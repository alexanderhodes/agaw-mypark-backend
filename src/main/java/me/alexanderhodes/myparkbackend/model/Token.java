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

    @Id
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    private LocalDateTime validUntil;

    public Token(String id, User user, LocalDateTime validUntil) {
        this.id = id;
        this.user = user;
        this.validUntil = validUntil;
    }
}
