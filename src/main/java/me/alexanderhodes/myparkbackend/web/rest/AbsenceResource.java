package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Absence;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AbsenceService;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api")
public class AbsenceResource {

    @Autowired
    private AbsenceService absenceService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/absences/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Absence>> getAbsencesForUser() {
        User user = this.authenticationService.getCurrentUser();
        if (user != null) {
            List<Absence> absences = this.absenceService.findByUser(user);
            return ResponseEntity.ok(absences);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/absences")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SYSTEM')")
    public ResponseEntity<Absence> createAbsence(@RequestBody Absence absence) {
        String id = this.uuidGenerator.newId();
        absence.setId(id);

        if (absence.getUser() == null) {
            User user = this.authenticationService.getCurrentUser();
            absence.setUser(user);
        }
        this.absenceService.save(absence);

        return ResponseEntity.ok(absence);
    }

    @PutMapping("/absences/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Absence> updateAbsence(@RequestBody Absence absence, @PathVariable String id) {
        absence.setId(id);
        this.absenceService.save(absence);

        return ResponseEntity.ok(absence);
    }

    @DeleteMapping("/absences/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteAbsence(@PathVariable("id") String id) {
        this.absenceService.deleteById(id);
    }

    @DeleteMapping("/absences/system")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity doAbsenceHousekeeping() {
        LocalDate today = LocalDate.now();
        try {
            this.absenceService.deleteByEndBefore(today);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

}
