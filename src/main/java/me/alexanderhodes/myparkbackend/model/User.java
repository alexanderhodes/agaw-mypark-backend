package me.alexanderhodes.myparkbackend.model;

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
    @Column(name = "privateEmail")
    private String privateEmail;
    @ManyToOne(optional = true)
    @JoinColumn(name = "f_parkingSpace")
    private ParkingSpace parkingSpace;

    public User(String id, String name, String password, String username, String firstName, String lastName,
                boolean enabled, ParkingSpace parkingSpace, String privateEmail) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.parkingSpace = parkingSpace;
        this.privateEmail = privateEmail;
    }

    public User toJson() {
        return new User(this.id, this.name, null, this.username, this.firstName, this.lastName, this.enabled,
                this.parkingSpace, this.privateEmail);
    }
}
