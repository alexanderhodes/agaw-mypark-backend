package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import org.springframework.data.repository.CrudRepository;

public interface ParkingSpaceService extends CrudRepository<ParkingSpace, Long> {
}
