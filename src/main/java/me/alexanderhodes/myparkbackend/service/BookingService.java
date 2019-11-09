package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Booking;
import org.springframework.data.repository.CrudRepository;

public interface BookingService extends CrudRepository<Booking, Long> {
}
