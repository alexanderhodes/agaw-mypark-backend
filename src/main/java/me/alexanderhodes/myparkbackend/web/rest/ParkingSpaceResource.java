package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.ParkingSpaceComparator;
import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.helper.ParkingSpaceUser;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceService;
import me.alexanderhodes.myparkbackend.service.ParkingSpaceStatusService;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private UserService userService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/parkingspaces")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpace>> getParkingSpaces() {
        List<ParkingSpace> list = this.parkingSpaceService.findAllByOrderByNumber();

        list.sort(new ParkingSpaceComparator());

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

            this.parkingSpaceService.save(parkingSpace);

            return ResponseEntity.ok(parkingSpace);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> getParkingSpace(@PathVariable("id") String id) {
        Optional<ParkingSpace> optional = this.parkingSpaceService.findById(id);

        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpace> updateParkingSpace(@RequestBody ParkingSpace parkingSpace,
                                                           @PathVariable("id") String id) {
        parkingSpace.setId(id);
        this.parkingSpaceService.save(parkingSpace);

        return ResponseEntity.ok(parkingSpace);
    }

    @DeleteMapping("/parkingspaces/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteParkingSpace(@PathVariable("id") String id) {
        Optional<ParkingSpace> optional = this.parkingSpaceService.findById(id);

        if (optional.isPresent()) {
            ParkingSpace parkingSpace = optional.get();
            // set parkingspace of user to null
            User user = this.userService.findByParkingSpace(parkingSpace);
            if (user != null) {
                user.setParkingSpace(null);
                this.userService.save(user);
            }

            this.parkingSpaceService.delete(parkingSpace);
        }
    }

    @GetMapping("/parkingspaces/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParkingSpaceUser>> findParkingSpacesWithUser() {
        List<ParkingSpaceUser> list = this.parkingSpaceService.findAllWithUser();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/parkingspaces/system/free/{day}")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<ParkingSpace>> findFreeParkingSpacesForDay(@PathVariable("day") String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        List<ParkingSpace> list = this.parkingSpaceService.findFreeParkingSpacesForDay(localDate);

        return ResponseEntity.ok(list);
    }


    @GetMapping("/parkingspaces/system/update")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity<List<ParkingSpace>> updateParkingSpaceStatus() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime start = localDate.atTime(0, 0);
        LocalDateTime end = localDate.atTime(23, 59);

        ParkingSpaceStatus free = this.parkingSpaceStatusService.findByName(ParkingSpaceStatus.FREE);
        ParkingSpaceStatus used = this.parkingSpaceStatusService.findByName(ParkingSpaceStatus.USED);

        // alle als belegt markieren
        List<ParkingSpace> allParkingSpaces = this.parkingSpaceService.findAllByOrderByNumber();
        allParkingSpaces.forEach(parkingSpace -> {
            parkingSpace.setParkingSpaceStatus(used);
            this.parkingSpaceService.save(parkingSpace);
        });
        // freie Parkplätze durch Freigaben markieren
        List<ParkingSpace> freeParkingSpaces = this.parkingSpaceService.findFreeParkingSpacesForDay(localDate);
        freeParkingSpaces.forEach(parkingSpace -> {
            parkingSpace.setParkingSpaceStatus(free);
            this.parkingSpaceService.save(parkingSpace);
        });
        // belegte Parkplätze durch Buchungen
        List<ParkingSpace> bookedParkingSpaces = this.parkingSpaceService.findBookedParkingSpacesForDay(start, end);
        bookedParkingSpaces.forEach(parkingSpace -> {
            parkingSpace.setParkingSpaceStatus(used);
            this.parkingSpaceService.save(parkingSpace);
        });

        List<ParkingSpace> parkingSpaces = this.parkingSpaceService.findAllByOrderByNumber();
        return ResponseEntity.ok(parkingSpaces);
    }

}
