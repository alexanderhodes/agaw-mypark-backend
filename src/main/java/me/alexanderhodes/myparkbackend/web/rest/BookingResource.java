package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.BookingService;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Booking> getBookings () {
        List<Booking> bookings = new ArrayList<>();

        bookingService.findAll().forEach(booking -> {
            bookings.add(booking);
        });

        return bookings;
    }

    @GetMapping("/bookings/users/{user}")
    public List<Booking> getBookingsForUser (@PathVariable("user") String username) {
        List<Booking> bookings = new ArrayList<>();

        User user = userService.findByUsername(username);
        if (user != null) {
            List<Booking> foundBookings = bookingService.findByUser(user);
            bookings.addAll(foundBookings);
        }

        return bookings;
    }

    @PostMapping("/bookings")
    public Booking createBooking (@RequestBody Booking booking) {
        bookingService.save(booking);

        return booking;
    }

    @GetMapping("/bookings/{id}")
    public Booking getBooking (@PathVariable("id") long id) {
        Optional<Booking> optionalBooking = bookingService.findById(id);
        Booking booking = optionalBooking.get();

        return booking;
    }

    @PutMapping("/bookings/{id}")
    public Booking updateBooking (@RequestBody Booking booking, @PathVariable("id") long id) {
        bookingService.save(booking);

        return booking;
    }

    @DeleteMapping("/bookings/{id}")
    public void deleteBooking (@PathVariable("id") long id) {
        bookingService.deleteById(id);
    }

}
