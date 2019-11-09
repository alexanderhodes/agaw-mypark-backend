package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mp_booking")
@Data
@NoArgsConstructor
public class Booking extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    @ManyToOne(optional = true)
    @JoinColumn(name = "f_parkingSpace")
    private ParkingSpace parkingSpace;
    @Column(name = "date")
    private Date date;

    public Booking(User user, ParkingSpace parkingSpace, Date date) {
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.date = date;
    }
}
