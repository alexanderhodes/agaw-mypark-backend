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

    @GetMapping("/common/password/validation/{token}")
    public ResponseEntity<User> validatePasswordResetToken(@PathVariable("token") String base64Token) {
        User user = commonService.validateToken(base64Token);

        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/common/password/reset/{token}")
    public ResponseEntity<Object> setPassword(@PathVariable("token") String base64Token, @RequestBody String body) {
        User user = commonService.validateToken(base64Token);

        if (user != null) {
            return commonService.storePassword(base64Token, body) ? ResponseEntity.ok().build() :
                    ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/common/register")
    public ResponseEntity<User> register(@RequestBody User body) {
        String uuid = uuidGenerator.newId();
        body.setId(uuid);

        User user = this.commonService.createUser(body);

        return (user == null) ? new ResponseEntity(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity(user.toJson(), HttpStatus.CREATED);
    }

    @GetMapping("/common/register/validation/{token}")
    public ResponseEntity validateRegisterToken(@PathVariable("token") String base64Token) {
        User user = this.commonService.validateToken(base64Token);

        if (user != null) {
            // activate user
            this.commonService.activateUser(user);
            // remove token
            this.commonService.deleteToken(base64Token);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}
