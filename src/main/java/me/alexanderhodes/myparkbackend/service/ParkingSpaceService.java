package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.helper.ParkingSpaceUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ParkingSpaceService extends CrudRepository<ParkingSpace, String> {

    public ParkingSpace findByNumber (String number);

    public List<ParkingSpace> findAllByOrderByNumber ();

    public List<ParkingSpace> findAll (Sort sort);

    public void deleteById (String id);

    @Query("select new me.alexanderhodes.myparkbackend.model.helper.ParkingSpaceUser(p, u) from ParkingSpace p " +
            "left join User u on (p.id = u.parkingSpace.id) order by p.number")
    public List<ParkingSpaceUser> findAllWithUser ();

    @Query("SELECT p FROM ParkingSpace p WHERE " +
            "p.id NOT IN (SELECT b.parkingSpace.id FROM User b WHERE b.parkingSpace IS NOT NULL) " +
            "OR p.id IN (SELECT c.id FROM ParkingSpace c WHERE c.id IN (" +
            "SELECT d.parkingSpace.id FROM User d WHERE d.id IN (" +
            "SELECT DISTINCT e.user.id FROM Absence e WHERE e.user.id in " +
            "(SELECT f.id FROM User f WHERE f.parkingSpace IS NOT NULL) " +
            "AND :date BETWEEN e.start AND e.end" +
            "))) order by p.number")
    public List<ParkingSpace> findFreeParkingSpacesForDay(@Param("date") LocalDate localDate);

}
