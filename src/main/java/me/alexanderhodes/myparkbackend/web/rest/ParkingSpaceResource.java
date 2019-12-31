package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceService;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpace>> getParkingSpaces() {
        List<ParkingSpace> list = new ArrayList<>();
        parkingSpaceService.findAll().forEach(parkingSpace -> {
            list.add(parkingSpace);
        });

        return ResponseEntity.ok(list);
    }

    @PostMapping("/parkingspaces")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        String number = parkingSpace.getNumber();
        ParkingSpace existingParkingSpace = this.parkingSpaceService.findByNumber(number);

        if (existingParkingSpace == null) {
            ParkingSpaceStatus parkingSpaceStatus = this.parkingSpaceStatusService.findByName(ParkingSpaceStatus.FREE);
            String uuid = this.uuidGenerator.newId();

            parkingSpace.setId(uuid);
            parkingSpace.setParkingSpaceStatus(parkingSpaceStatus);

            parkingSpaceService.save(parkingSpace);

            return ResponseEntity.ok(parkingSpace);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> getParkingSpace(@PathVariable("id") String id) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);

        return parkingSpace != null ? ResponseEntity.ok(parkingSpace) : ResponseEntity.notFound().build();
    }

    @PutMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> updateParkingSpace(@RequestBody ParkingSpace parkingSpace,
                                                           @PathVariable("id") String id) {
        parkingSpace.setId(id);
        parkingSpaceService.save(parkingSpace);

        return ResponseEntity.ok(parkingSpace);
    }

    @DeleteMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteParkingSpace(@PathVariable("id") long id) {
        parkingSpaceService.deleteById(id);
    }

}
