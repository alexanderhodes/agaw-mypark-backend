package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "mp_seriesabsence")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class SeriesAbsence implements Serializable {

    @Id
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    @Column(name = "active")
    private boolean active;
    @Column(name = "time")
    private LocalTime time;
    @Column(name = "weekday")
    private int weekday;

    public SeriesAbsence(String id, User user, boolean active, LocalTime time, int weekday) {
        this.id = id;
        this.user = user;
        this.active = active;
        this.time = time;
        this.weekday = weekday;
    }
}
