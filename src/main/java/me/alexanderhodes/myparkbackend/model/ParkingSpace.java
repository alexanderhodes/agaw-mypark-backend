package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_parkingspace")
@Data
@NoArgsConstructor
public class ParkingSpace extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "number")
    private String number;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_parkingSpaceStatus")
    private ParkingSpaceStatus parkingSpaceStatus;

    public ParkingSpace(String number, ParkingSpaceStatus parkingSpaceStatus) {
        this.number = number;
        this.parkingSpaceStatus = parkingSpaceStatus;
    }
}
