package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "mp_absence")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Absence implements Serializable {

    @Id
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    @Column(name = "start_date")
    private LocalDate start;
    @Column(name = "end_date")
    private LocalDate end;

    public Absence(User user, LocalDate start, LocalDate end) {
        this.user = user;
        this.start = start;
        this.end = end;
    }
}
