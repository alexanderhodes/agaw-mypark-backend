package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.SeriesAbsence;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.SeriesAbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class SeriesAbsenceResource {

    @Autowired
    private SeriesAbsenceService seriesAbsenceService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/seriesabsences")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesAbsence>> getSeriesAbsencesForUser() {
        User user = this.authenticationService.getCurrentUser();

        List<SeriesAbsence> seriesAbsences = this.seriesAbsenceService.findByUser(user);

        return ResponseEntity.ok(seriesAbsences);
    }

    @PostMapping("/seriesabsences")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesAbsence>> createSeriesAbsences(@RequestBody List<SeriesAbsence> seriesAbsences) {
        if (seriesAbsences != null) {
            User user = this.authenticationService.getCurrentUser();

            seriesAbsences.forEach(seriesAbsence -> {
                String id = uuidGenerator.newId();
                seriesAbsence.setId(id);
                seriesAbsence.setUser(user);

                seriesAbsenceService.save(seriesAbsence);
            });

            return ResponseEntity.ok(seriesAbsences);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/seriesabsences")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<SeriesAbsence>> updateSeriesAbsences(@RequestBody List<SeriesAbsence> seriesAbsences) {
        if (seriesAbsences != null) {
            User user = this.authenticationService.getCurrentUser();

            seriesAbsences.forEach(seriesAbsence -> {
                if (seriesAbsence.getId() == null) {
                    String id = uuidGenerator.newId();
                    seriesAbsence.setId(id);
                }
                seriesAbsence.setUser(user);
                seriesAbsenceService.save(seriesAbsence);
            });

            return ResponseEntity.ok(seriesAbsences);
        }
        return ResponseEntity.badRequest().build();
    }

}
