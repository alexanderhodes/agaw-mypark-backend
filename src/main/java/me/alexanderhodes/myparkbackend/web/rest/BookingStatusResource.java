package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.BookingStatus;
import me.alexanderhodes.myparkbackend.service.BookingStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class BookingStatusResource {

    @Autowired
    private BookingStatusService bookingStatusService;

    @GetMapping("/bookingstatus")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<BookingStatus>> getAllBookingStatus() {
        List<BookingStatus> bookingStatuses = this.bookingStatusService.findAll();

        return ResponseEntity.ok(bookingStatuses);
    }

}
