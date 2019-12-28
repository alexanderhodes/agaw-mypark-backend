package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_authorities")
@Data
@NoArgsConstructor
@IdClass(UserRoleId.class)
public class UserRole implements Serializable {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_role")
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

}
