package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "mp_bookingstatus")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class BookingStatus implements Serializable {

    public static final String REQUEST = "requested";
    public static final String CONFIRMED = "confirmed";
    public static final String OCCUPIED = "occupied";
    public static final String DECLINED = "declined";

    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;

    public BookingStatus(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
