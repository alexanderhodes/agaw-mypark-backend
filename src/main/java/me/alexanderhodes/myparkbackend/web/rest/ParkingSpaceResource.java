package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/parkingspaces")
    public List<ParkingSpace> getParkingSpaces() {
        List<ParkingSpace> list = new ArrayList<>();
        parkingSpaceService.findAll().forEach(parkingSpace -> {
            list.add(parkingSpace);
        });

        return list;
    }

    @PostMapping("/parkingspaces")
    public ParkingSpace createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
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
    public ParkingSpace updateParkingSpace(@RequestBody ParkingSpace parkingSpace, @PathVariable("id") long id) {
        parkingSpaceService.save(parkingSpace);

        return parkingSpace;
    }

    @DeleteMapping("/parkingspaces/{id}")
    public void deleteParkingSpace(@PathVariable("id") long id) {
        parkingSpaceService.deleteById(id);
    }

}
