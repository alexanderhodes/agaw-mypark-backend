package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceService;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ParkingSpaceResource {

    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private ParkingSpaceStatusService parkingSpaceStatusService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/parkingspaces")
    public List<ParkingSpace> getParkingSpaces() {
        List<ParkingSpace> list = new ArrayList<>();
        parkingSpaceService.findAll().forEach(parkingSpace -> {
            list.add(parkingSpace);
        });

        return list;
    }

    @PostMapping("/parkingspaces")
    @Secured({"ADMIN"})
    public ParkingSpace createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        ParkingSpaceStatus parkingSpaceStatus = this.parkingSpaceStatusService.findByName(ParkingSpaceStatus.FREE);
        String uuid = this.uuidGenerator.newId();

        parkingSpace.setId(uuid);
        parkingSpace.setParkingSpaceStatus(parkingSpaceStatus);

        parkingSpaceService.save(parkingSpace);

        return parkingSpace;
    }

    @GetMapping("/parkingspaces/{id}")
    public ParkingSpace getParkingSpace(@PathVariable("id") long id) {
        Optional<ParkingSpace> optionalParkingSpace = parkingSpaceService.findById(id);
        ParkingSpace parkingSpace = optionalParkingSpace.get();

        return parkingSpace;
    }

    @PutMapping("/parkingspaces/{id}")
    @Secured({"ADMIN"})
    public ParkingSpace updateParkingSpace(@RequestBody ParkingSpace parkingSpace, @PathVariable("id") long id) {
        parkingSpaceService.save(parkingSpace);

        return parkingSpace;
    }

    @DeleteMapping("/parkingspaces/{id}")
    @Secured({"ADMIN"})
    public void deleteParkingSpace(@PathVariable("id") long id) {
        parkingSpaceService.deleteById(id);
    }

}
