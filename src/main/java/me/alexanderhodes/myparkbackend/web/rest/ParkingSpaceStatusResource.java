package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ParkingSpaceStatusResource {

    @Autowired
    private ParkingSpaceStatusService parkingSpaceStatusService;

    @GetMapping("/parkingspacestatus")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ParkingSpaceStatus> getParkingSpaceStatus() {
        List<ParkingSpaceStatus> list = new ArrayList<>();
        parkingSpaceStatusService.findAll().forEach(parkingSpaceStatus -> {
            list.add(parkingSpaceStatus);
        });

        return list;
    }

    @PostMapping("/parkingspacestatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ParkingSpaceStatus createParkingSpaceStatus(@RequestBody ParkingSpaceStatus parkingSpaceStatus) {
        parkingSpaceStatusService.save(parkingSpaceStatus);

        return parkingSpaceStatus;
    }

    @GetMapping("/parkingspacestatus/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ParkingSpaceStatus getParkingSpaceStatus(@PathParam("id") long id) {
        Optional<ParkingSpaceStatus> optionalParkingSpaceStatus = parkingSpaceStatusService.findById(id);
        ParkingSpaceStatus parkingSpaceStatus = optionalParkingSpaceStatus.get();

        return parkingSpaceStatus;
    }

    @PutMapping("/parkingspacestatus/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ParkingSpaceStatus updateParkingSpaceStatus(@RequestBody ParkingSpaceStatus parkingSpaceStatus, @PathVariable("id") long id) {
        parkingSpaceStatusService.save(parkingSpaceStatus);

        return parkingSpaceStatus;
    }

    @DeleteMapping("/parkingspacestatus/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteParkingSpaceStatus(@PathVariable("id") long id) {
        parkingSpaceStatusService.deleteById(id);
    }

}
