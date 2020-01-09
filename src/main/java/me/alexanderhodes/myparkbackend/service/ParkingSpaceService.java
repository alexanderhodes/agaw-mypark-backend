package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParkingSpaceService extends CrudRepository<ParkingSpace, String> {

    public ParkingSpace findByNumber (String number);

    public List<ParkingSpace> findAllByOrderByNumber ();

    public List<ParkingSpace> findAll (Sort sort);

    public void deleteById (String id);

}
