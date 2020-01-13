package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.mail.MailHelper;
import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.mail.model.MMail;
import me.alexanderhodes.myparkbackend.model.*;
import me.alexanderhodes.myparkbackend.service.*;
import me.alexanderhodes.myparkbackend.translations.EmailTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class BookingResource {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UuidGenerator uuidGenerator;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private BookingStatusService bookingStatusService;
    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private ParkingSpaceStatusService parkingSpaceStatusService;
    @Autowired
    private MailService mailService;
    @Autowired
    private MailHelper mailHelper;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookings() {
        List<Booking> bookings = new ArrayList<>();

        bookingService.findAll().forEach(booking -> {
            bookings.add(booking);
        });

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/users")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsForUser() {
        User user = this.authenticationService.getCurrentUser();
        if (user != null) {
            List<Booking> bookings = bookingService.findByUserOrderByDateAsc(user);
            return ResponseEntity.ok(bookings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bookings/users/{day}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> getBookingForUserAndDay(@PathVariable("day") String date) {
        Booking booking = this.findBookingForDay(date, null);

        return ResponseEntity.ok(booking);
    }

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking body) {
        String day = body.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        Booking booking = this.findBookingForDay(day, body.getUser());

        if (booking == null) {
            if (body.getDate().toLocalDate().equals(LocalDate.now())) {
                if (body.getParkingSpace() == null) {
                    // 1. heute -> Parkplätze sind frei
                    List<ParkingSpace> parkingSpaces = this.parkingSpaceService
                            .findByParkingSpaceStatus(ParkingSpaceStatus.FREE);
                    if (parkingSpaces.size() > 0) {
                        BookingStatus bookingStatus = this.bookingStatusService.findByName(BookingStatus.CONFIRMED);
                        body.setBookingStatus(bookingStatus);
                        body.setParkingSpace(parkingSpaces.get(0));
                    }
                } else {
                    // 2. heute -> mit Parkplatz
                    BookingStatus bookingStatus = this.bookingStatusService.findByName(BookingStatus.CONFIRMED);
                    body.setBookingStatus(bookingStatus);
                }
                ParkingSpaceStatus parkingSpaceStatus =
                        this.parkingSpaceStatusService.findByName(ParkingSpaceStatus.USED);
                ParkingSpace parkingSpace = body.getParkingSpace();
                parkingSpace.setParkingSpaceStatus(parkingSpaceStatus);
                this.parkingSpaceService.save(parkingSpace);
            } else {
                // 3. in Zukunft
                // 4. morgen
                BookingStatus bookingStatus = this.bookingStatusService.findByName(BookingStatus.REQUEST);
                body.setBookingStatus(bookingStatus);
            }

            // Prüfen, ob Benutzer dabei ist
            if (body.getUser() == null) {
                User user = this.authenticationService.getCurrentUser();
                body.setUser(user);
            }
            // neue Id generieren
            String id = uuidGenerator.newId();
            body.setId(id);
            // speichern
            bookingService.save(body);
            // Prüfen, ob E-Mail Versand notwendig ist
            if (body.getBookingStatus().getName().equals(BookingStatus.CONFIRMED)) {
                this.sendConfirmationMail(body);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") String id) {
        Optional<Booking> optional = bookingService.findById(id);

        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking body, @PathVariable("id") String id) {
        Optional<Booking> optional = this.bookingService.findById(id);

        if (optional.isPresent()) {
            Booking oldState = optional.get();
            if (oldState.getBookingStatus().getName().equals(BookingStatus.REQUEST) &&
                    body.getBookingStatus().getName().equals(BookingStatus.CONFIRMED)) {
                // ParkingSpace has been assigned to booking -> mail has to be sent
                this.sendConfirmationMail(body);
            }
        }

        body.setId(id);
        bookingService.save(body);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteBooking(@PathVariable("id") String id) {
        bookingService.deleteById(id);
    }

    @DeleteMapping("/bookings/system")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity doBookingHousekeeping() {
        LocalDateTime now = LocalDateTime.now();
        try {
            this.bookingService.deleteByDateBefore(now);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookings/system/{day}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<Booking>> getBookingsForDay(@PathVariable("day") String date) {
        String dateTime = new StringBuffer(date).append(" 00:00:00").toString();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        LocalDateTime after = localDateTime.minusDays(1).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime before = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        List<Booking> bookings = bookingService.findByDateAfterAndDateBefore(after, before);

        return ResponseEntity.ok(bookings);
    }

    private Booking findBookingForDay(String date, User user) {
        String dateTime = new StringBuffer(date).append(" 00:00:00").toString();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        if (user == null) {
            user = this.authenticationService.getCurrentUser();
        }
        LocalDateTime after = localDateTime.minusDays(1).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime before = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        List<Booking> bookings = bookingService.findByUserAndDateAfterAndDateBefore(user, after, before);

        return bookings != null && bookings.size() > 0 ? bookings.get(0) : null;
    }

    private void sendConfirmationMail(Booking booking) {
        String receiver = booking.getUser().getUsername();
        String parkingSpace = booking.getParkingSpace().getNumber();
        String username = booking.getUser().getFirstName() + " " + booking.getUser().getLastName();

        List<AbstractMap.SimpleEntry<String, String>> placeholders = new ArrayList<>();
        placeholders.add(new AbstractMap.SimpleEntry<>("PLACEHOLDER_PARKINGSPACE", parkingSpace));

        MMail mail = mailHelper.createMail(receiver, username, EmailTranslations.BOOKING_SUCCESS_TODAY,
                placeholders);

        try {
            this.mailService.send(mail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
