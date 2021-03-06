package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpaceStatus;
import org.springframework.data.repository.CrudRepository;

public interface ParkingSpaceStatusService extends CrudRepository<ParkingSpaceStatus, String> {

    public ParkingSpaceStatus findByName (String name);

}
