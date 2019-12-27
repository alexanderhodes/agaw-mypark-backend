package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class UnauthorizedResource {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/common/password/request/")
    public ResponseEntity<User> requestPasswordReset(@RequestParam(value = "email") String email) {
        User user = passwordResetService.requestPasswordReset(email);

        return (user == null) ? new ResponseEntity(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping("/common/password/reset/{token}")
    public void setPassword(@PathVariable("token") String token) {
        // prüfen, ob alles existiert und Eingaben stimmen
        System.out.println("token " + token);
    }


}
