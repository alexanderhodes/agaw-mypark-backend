package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_users")
@Data
@NoArgsConstructor
public class User extends CommonEntity implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "enabled")
    private boolean enabled;

    public User(String name, String password, String username, String firstName, String lastName, boolean enabled) {
        this.name = name;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }
}
