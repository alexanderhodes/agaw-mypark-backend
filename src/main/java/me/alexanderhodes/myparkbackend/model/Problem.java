package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mp_problem")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Problem implements Serializable {

    @Id
    private String id;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "reason")
    private String reason;
    @ManyToOne(optional = true)
    @JoinColumn(name = "f_parkingSpace")
    private ParkingSpace parkingSpace;
    @ManyToOne(optional = true)
    @JoinColumn(name = "f_user")
    private User user;

    public Problem(String id, LocalDateTime date, String reason, ParkingSpace parkingSpace, User user) {
        this.id = id;
        this.date = date;
        this.reason = reason;
        this.parkingSpace = parkingSpace;
        this.user = user;
    }
}
