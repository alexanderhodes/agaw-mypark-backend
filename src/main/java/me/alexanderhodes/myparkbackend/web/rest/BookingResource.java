package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.BookingService;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class BookingResource {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UuidGenerator uuidGenerator;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookings () {
        List<Booking> bookings = new ArrayList<>();

        bookingService.findAll().forEach(booking -> {
            bookings.add(booking);
        });

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/users/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsForUser () {
        String username = this.authenticationService.getCurrentUsername();

        if (username != null && !username.isEmpty()) {
            User user = userService.findByUsername(username);
            if (user != null) {
                List<Booking> bookings = bookingService.findByUserOrderByDateAsc(user);
                return ResponseEntity.ok(bookings);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> createBooking (@RequestBody Booking booking) {
        // ToDo: check if user has already a booking for this day
        String id = uuidGenerator.newId();
        booking.setId(id);
        bookingService.save(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> getBooking (@PathVariable("id") String id) {
        Booking booking = bookingService.findById(id);

        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Booking> updateBooking (@RequestBody Booking booking, @PathVariable("id") String id) {
        booking.setId(id);
        bookingService.save(booking);

        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteBooking (@PathVariable("id") long id) {
        bookingService.deleteById(id);
    }

}
