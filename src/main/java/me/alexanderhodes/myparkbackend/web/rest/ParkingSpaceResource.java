package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class ParkingSpaceResource {

    @GetMapping("/parkingspace")
    public List<ParkingSpace> getParkingSpaces() {
        return new ArrayList<ParkingSpace>();
    }

}
