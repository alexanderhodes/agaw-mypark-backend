package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers () {
        List<User> users = new ArrayList<>();
        userService.findAll().forEach(user -> {
            users.add(user);
        });
//        User[] users = new User[]{
//                new User("alex", "password", "mail@mail.com")
//        };
//        return Arrays.asList(users);
        return users;
    }

    @GetMapping("/user")
    public User getUser (@RequestParam(value = "name", defaultValue = "username") String username) {
        return new User(username, "password", "mail@mail.de", "max", "mustermann");
    }

    @GetMapping("/save")
    public User save() {
        User user = new User("alex", "password", "password", "john", "doe");
        userService.save(user);
        return user;
    }

    @PostMapping("/users")
    public User store(@RequestBody User user) {
        System.out.println(user);
        return user;
    }

}
