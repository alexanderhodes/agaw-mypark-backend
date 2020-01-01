package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Problem;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ProblemResource {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private UuidGenerator uuidGenerator;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/problems")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Problem>> getProblems() {
        List<Problem> problems = new ArrayList<>();

        problemService.findAll().forEach(problem -> problems.add(problem));

        return ResponseEntity.ok(problems);
    }

    @PostMapping("/problems")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Problem> createProblem(@RequestBody Problem problem) {
        String id = uuidGenerator.newId();
        User user = authenticationService.getCurrentUser();

        problem.setId(id);
        problem.setUser(user);

        problemService.save(problem);

        return ResponseEntity.status(HttpStatus.CREATED).body(problem);
    }

    @GetMapping("/problems/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Problem> getProblem(@PathVariable("id") String id) {
        Optional<Problem> optional = problemService.findById(id);

        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/problem/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Problem> updateProblem(@RequestBody Problem problem, @PathVariable("id") String id) {
        problem.setId(id);
        problemService.save(problem);

        return ResponseEntity.ok(problem);
    }

    @DeleteMapping("/problems/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteProblem(@PathVariable String id) {
        problemService.deleteById(id);
    }

}
