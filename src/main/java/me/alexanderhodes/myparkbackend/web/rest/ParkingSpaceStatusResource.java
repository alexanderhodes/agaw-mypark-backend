package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ParkingSpaceStatus> getParkingSpaceStatus() {
        List<ParkingSpaceStatus> list = new ArrayList<>();
        parkingSpaceStatusService.findAll().forEach(parkingSpaceStatus -> {
            list.add(parkingSpaceStatus);
        });

        return list;
    }

    @PostMapping("/parkingspacestatus")
    public ParkingSpaceStatus createParkingSpaceStatus(@RequestBody ParkingSpaceStatus parkingSpaceStatus) {
        parkingSpaceStatusService.save(parkingSpaceStatus);

        return parkingSpaceStatus;
    }

    @GetMapping("/parkingspacestatus/{id}")
    public ParkingSpaceStatus getParkingSpaceStatus(@PathParam("id") long id) {
        Optional<ParkingSpaceStatus> optionalParkingSpaceStatus = parkingSpaceStatusService.findById(id);
        ParkingSpaceStatus parkingSpaceStatus = optionalParkingSpaceStatus.get();

        return parkingSpaceStatus;
    }

    @PutMapping("/parkingspacestatus/{id}")
    public ParkingSpaceStatus updateParkingSpaceStatus(@RequestBody ParkingSpaceStatus parkingSpaceStatus, @PathVariable("id") long id) {
        parkingSpaceStatusService.save(parkingSpaceStatus);

        return parkingSpaceStatus;
    }

    @DeleteMapping("/parkingspacestatus/{id}")
    public void deleteParkingSpaceStatus(@PathVariable("id") long id) {
        parkingSpaceStatusService.deleteById(id);
    }

}
