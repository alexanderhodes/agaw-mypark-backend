package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_parkingspacestatus")
@Data
@NoArgsConstructor
public class ParkingSpaceStatus extends CommonEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;

    public ParkingSpaceStatus(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
