package me.alexanderhodes.myparkbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mp_role")
@Data
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @Column(name = "name")
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
