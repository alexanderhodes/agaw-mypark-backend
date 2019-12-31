package me.alexanderhodes.myparkbackend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mp_booking")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Booking extends CommonEntity implements Serializable {

    @Id
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_user")
    private User user;
    @ManyToOne(optional = true)
    @JoinColumn(name = "f_parkingSpace")
    private ParkingSpace parkingSpace;
    @Column(name = "date")
    private Date date;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_bookingStatus")
    private BookingStatus bookingStatus;

    public Booking(User user, ParkingSpace parkingSpace, Date date) {
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.date = date;
    }
}
