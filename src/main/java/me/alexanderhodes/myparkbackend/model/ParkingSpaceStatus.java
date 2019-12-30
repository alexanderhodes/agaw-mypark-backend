package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_parkingspacestatus")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class ParkingSpaceStatus extends CommonEntity implements Serializable {

    public static final String FREE = "free";
    public static final String USED = "used";

    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;

    public ParkingSpaceStatus(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
