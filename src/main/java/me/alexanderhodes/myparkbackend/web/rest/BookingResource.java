package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class BookingResource {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public List<Booking> getBookings () {
        List<Booking> bookings = new ArrayList<>();

        bookingService.findAll().forEach(booking -> {
            bookings.add(booking);
        });

        return bookings;
    }

    @PostMapping("/bookings")
    public Booking createBooking (@RequestBody Booking booking) {
        bookingService.save(booking);

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
