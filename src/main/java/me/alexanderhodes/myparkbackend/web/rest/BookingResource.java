package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.BookingService;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class BookingResource {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookings () {
        List<Booking> bookings = new ArrayList<>();

        bookingService.findAll().forEach(booking -> {
            bookings.add(booking);
        });

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/users/{user}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getBookingsForUser (@PathVariable("user") String username) {
        List<Booking> bookings = new ArrayList<>();

        User user = userService.findByUsername(username);
        if (user != null) {
            List<Booking> foundBookings = bookingService.findByUser(user);
            bookings.addAll(foundBookings);
        }

        return ResponseEntity.ok(bookings);
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
