package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime date;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_bookingStatus")
    private BookingStatus bookingStatus;

    public Booking(User user, ParkingSpace parkingSpace, LocalDateTime date) {
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.date = date;
    }

    public Booking(String id, User user, ParkingSpace parkingSpace, LocalDateTime date, BookingStatus bookingStatus) {
        this.id = id;
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.date = date;
        this.bookingStatus = bookingStatus;
    }

    public User getUser() {
        return this.user == null ? null : this.user.toJson();
    }
}
