package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class CommonResource {

    @Autowired
    private CommonService commonService;
    @Autowired
    private UuidGenerator uuidGenerator;

    @GetMapping("/common/password/request/")
    public ResponseEntity<User> requestPasswordReset(@RequestParam(value = "email") String email) {
        User user = commonService.requestPasswordReset(email);

        return (user == null) ? new ResponseEntity(null, HttpStatus.NOT_FOUND) :
                new ResponseEntity(user, HttpStatus.OK);
    }

    @GetMapping("/common/password/reset/{token}")
    public void setPassword(@PathVariable("token") String token) {
        // prüfen, ob alles existiert und Eingaben stimmen
        System.out.println("token " + token);
    }

    @PostMapping("/common/register")
    public ResponseEntity<User> register(@RequestBody User body) {
        String uuid = uuidGenerator.newId();
        body.setId(uuid);

        User user = this.commonService.createUser(body);

        return (user == null) ? new ResponseEntity(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity(user.toJson(), HttpStatus.CREATED);
    }

}
