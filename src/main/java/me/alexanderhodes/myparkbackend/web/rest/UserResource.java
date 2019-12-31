package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class UserResource {

    @Autowired
    private UserService userService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers () {
        List<User> users = new ArrayList<>();
        userService.findAll().forEach(user -> {
            users.add(user.toJson());
        });
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String uuid = uuidGenerator.newId();
        user.setId(uuid);

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user.toJson());
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUser(@PathParam("id") long id) {
        Optional<User> optionalUser = userService.findById(id);

        return optionalUser.isPresent() ? ResponseEntity.ok(optionalUser.get().toJson()) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathParam("id") String id) {
        user.setId(id);
        userService.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathParam("id") long id) {
        userService.deleteById(id);
    }

}
