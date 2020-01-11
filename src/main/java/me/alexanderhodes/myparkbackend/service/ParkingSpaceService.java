package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.helper.ParkingSpaceUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParkingSpaceService extends CrudRepository<ParkingSpace, String> {

    public ParkingSpace findByNumber (String number);

    public List<ParkingSpace> findAllByOrderByNumber ();

    public List<ParkingSpace> findAll (Sort sort);

    public void deleteById (String id);

    @Query("select new me.alexanderhodes.myparkbackend.model.helper.ParkingSpaceUser(p, u) from ParkingSpace p " +
            "left join User u on (p.id = u.parkingSpace.id) order by p.number")
    public List<ParkingSpaceUser> findAllWithUser ();

}
