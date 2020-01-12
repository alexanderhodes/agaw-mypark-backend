package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api")
public class TokenResource {

    @Autowired
    private TokenService tokenService;

    @DeleteMapping("/token/system")
    @PreAuthorize("hasRole('SYSTEM')")
    public ResponseEntity doTokenHousekeeping() {
        LocalDateTime now = LocalDateTime.now();
        try {
            this.tokenService.deleteByValidUntilBefore(now);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

}
