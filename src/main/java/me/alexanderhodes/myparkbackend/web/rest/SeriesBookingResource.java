package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.SeriesBooking;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.SeriesBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class SeriesBookingResource {

    @Autowired
    private SeriesBookingService seriesBookingService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/seriesbookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesBooking>> getSeriesBookingsForUser() {
        User user = authenticationService.getCurrentUser();

        List<SeriesBooking> seriesBookings = seriesBookingService.findByUser(user);

        return ResponseEntity.ok(seriesBookings);
    }

    @PostMapping("/seriesbookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesBooking>> createSeriesBookings (@RequestBody List<SeriesBooking> list) {
        if (list != null) {
            User user = authenticationService.getCurrentUser();

            list.forEach(seriesBooking -> {
                String id = uuidGenerator.newId();
                seriesBooking.setId(id);
                seriesBooking.setUser(user);

                seriesBookingService.save(seriesBooking);
            });

            return ResponseEntity.ok(list);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/seriesbookings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesBooking>> updateSeriesBookings (@RequestBody List<SeriesBooking> list) {
        if (list != null) {
            User user = authenticationService.getCurrentUser();

            list.forEach(seriesBooking -> {
                if (seriesBooking.getId() == null) {
                    String id = uuidGenerator.newId();
                    seriesBooking.setId(id);
                }

                seriesBooking.setUser(user);
                seriesBookingService.save(seriesBooking);
            });
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/seriesbookings/system/{weekday}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<SeriesBooking>> findActiveSeriesBookingsByWeekDay(
            @PathVariable("weekday") int weekDay) {
        List<SeriesBooking> list = this.seriesBookingService.findActiveByWeekDay(weekDay);
        return ResponseEntity.ok(list);
    }
}
