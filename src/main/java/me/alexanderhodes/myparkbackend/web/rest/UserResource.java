package me.alexanderhodes.myparkbackend.web.rest;

import me.alexanderhodes.myparkbackend.helper.FormDataHandler;
import me.alexanderhodes.myparkbackend.helper.PasswordHelper;
import me.alexanderhodes.myparkbackend.helper.UuidGenerator;
import me.alexanderhodes.myparkbackend.model.Role;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.UserRole;
import me.alexanderhodes.myparkbackend.model.helper.UserAdmin;
import me.alexanderhodes.myparkbackend.service.AuthenticationService;
import me.alexanderhodes.myparkbackend.service.UserRoleService;
import me.alexanderhodes.myparkbackend.service.UserService;
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
public class UserResource {

    @Autowired
    private UserService userService;
    @Autowired
    private UuidGenerator uuidGenerator;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private FormDataHandler formDataHandler;
    @Autowired
    private PasswordHelper passwordHelper;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getEnabledUsers () {
        boolean enabled = true;
        List<User> users = userService.findAllByEnabled(enabled);
        List<User> response = new ArrayList<>();

        users.forEach(user -> response.add(user.toJson()));

        return ResponseEntity.ok(response);
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
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        Optional<User> optionalUser = userService.findById(id);

        return optionalUser.isPresent() ? ResponseEntity.ok(optionalUser.get().toJson()) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/users/current")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getCurrentUser() {
        User user = authenticationService.getCurrentUser();

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") String id) {
        user.setId(id);
        user.setEnabled(true);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            Optional<User> foundUser = this.userService.findById(id);

            if (foundUser.isPresent()) {
                user.setPassword(foundUser.get().getPassword());
            }
        }

        userService.save(user);

        return ResponseEntity.ok(user.toJson());
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") String id) {
        // ToDo: just set inactive that user can not login again
        Optional<User> optional = userService.findById(id);

        if (optional.isPresent()) {
            User user = optional.get();

            user.setEnabled(false);
            user.setParkingSpace(null);

            userService.save(user);
        }
    }

    @PutMapping("/users/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateAdminRole(@PathVariable("id") String id) {
        Role admin = new Role("ADMIN");
        User user = null;
        Optional<User> optional = userService.findById(id);

        if (optional.isPresent()) {
            user = optional.get();

            UserRole userRole = userRoleService.findByUserAndRole(user, admin);

            if (userRole != null) {
                // admin role exists and has to be deleted
                userRoleService.delete(userRole);
            } else {
                // admin role does not exist and has to be created
                userRole = new UserRole(user, admin);
                userRoleService.save(userRole);
            }
        }

        return ResponseEntity.ok(user.toJson());
    }

    @GetMapping("/users/parkingspace")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersWithParkingSpace(@RequestParam(value = "with", defaultValue = "false")
                                                                           boolean withParkingSpace) {
        List<User> users = withParkingSpace ? this.userService.findByParkingSpaceIsNotNull()
                : this.userService.findByParkingSpaceIsNull();

        for (int i = 0; i < users.size(); i++) {
            users.set(i, users.get(i).toJson());
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersWithRoleAdmin() {
        List<User> users = new ArrayList<>();
        Role admin = new Role("ADMIN");

        List<UserRole> userRoles = this.userRoleService.findByRole(admin);

        userRoles.forEach(userRole -> users.add(userRole.getUser().toJson()));

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/isadmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAdmin>> getUsersWithIsAdmin() {
        List<UserAdmin> users = this.userService.findByAdmin();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> changePassword(@RequestBody String formData) {
        User user = this.authenticationService.getCurrentUser();

        String currentPassword = this.formDataHandler.extract(formData, 3);

        if (this.passwordHelper.matches(currentPassword, user.getPassword())) {
            String newPassword = this.formDataHandler.extract(formData, 7);
            String newPasswordEncoded = this.passwordHelper.encode(newPassword);

            user.setPassword(newPasswordEncoded);
            this.userService.save(user);
            return ResponseEntity.ok(user.toJson());
        }
        return ResponseEntity.badRequest().build();
    }


}
