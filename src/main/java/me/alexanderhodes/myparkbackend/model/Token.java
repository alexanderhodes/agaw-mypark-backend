package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_token")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Token implements Serializable {

    public static final String PASSWORD_RESET = "password_reset";
    public static final String REGISTRATION = "registration";

    @Id
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    private LocalDateTime validUntil;
    @Column(name = "category")
    private String category;

    public Token(String id, User user, LocalDateTime validUntil, String category) {
        this.id = id;
        this.user = user;
        this.validUntil = validUntil;
        this.category = category;
    }
}
