package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.mail.MailHelper;
import me.alexanderhodes.myparkbackend.mail.MailService;
import me.alexanderhodes.myparkbackend.mail.model.MMail;
import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.BookingStatus;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.BookingService;
import me.alexanderhodes.myparkbackend.service.BookingStatusService;
import me.alexanderhodes.myparkbackend.translations.EmailTranslations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        String dateTime = new StringBuffer(date).append(" 00:00:00").toString();
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        User user = this.authenticationService.getCurrentUser();
        LocalDateTime after = localDateTime.minusDays(1).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime before = localDateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        List<Booking> bookings = bookingService.findByUserAndDateAfterAndDateBefore(user, after, before);

        return (bookings != null && bookings.size() > 0) ? ResponseEntity.ok(bookings.get(0)) : ResponseEntity.ok(null);
    }

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        // ToDo: if today - check for free parkingspaces for today and assign parkingspace
        if (booking.getParkingSpace() == null && booking.getBookingStatus() == null) {
            BookingStatus bookingStatus = this.bookingStatusService.findByName(BookingStatus.REQUEST);
            booking.setBookingStatus(bookingStatus);
        } else if (booking.getParkingSpace() != null && booking.getBookingStatus() == null) {
            BookingStatus bookingStatus = this.bookingStatusService.findByName(BookingStatus.CONFIRMED);
            booking.setBookingStatus(bookingStatus);
        }
        if (booking.getUser() == null) {
            User user = this.authenticationService.getCurrentUser();
            booking.setUser(user);
        }
        // ToDo: check if user has already a booking for this day
        String id = uuidGenerator.newId();
        booking.setId(id);
        bookingService.save(booking);
        // ToDo: send email
        if (booking.getBookingStatus().getName().equals(BookingStatus.CONFIRMED)) {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") String id) {
        Optional<Booking> optional = bookingService.findById(id);

        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> updateBooking(@RequestBody Booking booking, @PathVariable("id") String id) {
        booking.setId(id);
        bookingService.save(booking);

        return ResponseEntity.ok(booking);
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

}
