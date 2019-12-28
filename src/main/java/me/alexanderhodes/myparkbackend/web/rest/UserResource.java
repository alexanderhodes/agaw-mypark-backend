package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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
    public List<User> getUsers () {
        List<User> users = new ArrayList<>();
        userService.findAll().forEach(user -> {
            users.add(user.toJson());
        });
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        String uuid = uuidGenerator.newId();
        user.setId(uuid);

        userService.save(user);

        return user.toJson();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathParam("id") long id) {
        Optional<User> optionalUser = userService.findById(id);
        User user = optionalUser.get();

        return user.toJson();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathParam("id") long id) {
        userService.save(user);

        return user.toJson();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathParam("id") long id) {
        userService.deleteById(id);
    }

}
