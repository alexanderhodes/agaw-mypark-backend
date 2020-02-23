package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_parkingspace")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "number"})
public class ParkingSpace extends CommonEntity implements Serializable {

    @Id
    private String id;
    @Column(name = "number")
    private String number;
    @ManyToOne(optional = false)
    @JoinColumn(name = "f_parkingSpaceStatus")
    private ParkingSpaceStatus parkingSpaceStatus;

    public ParkingSpace(String id) {
        this.id = id;
    }

    public ParkingSpace(String number, ParkingSpaceStatus parkingSpaceStatus) {
        this.number = number;
        this.parkingSpaceStatus = parkingSpaceStatus;
    }

    public ParkingSpace(String id, String number, ParkingSpaceStatus parkingSpaceStatus) {
        this.id = id;
        this.number = number;
        this.parkingSpaceStatus = parkingSpaceStatus;
    }
}
