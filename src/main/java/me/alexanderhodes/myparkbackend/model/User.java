package me.alexanderhodes.myparkbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"username"})
public class User extends CommonEntity implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "enabled")
    private boolean enabled;

    public User(String id, String name, String password, String username, String firstName, String lastName,
                boolean enabled) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
    }

    public User toJson() {
        return new User(this.id, this.name, null, this.username, this.firstName, this.lastName, this.enabled);
    }
}
