package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingService extends CrudRepository<Booking, Long> {

    public List<Booking> findByUser (User user);

    public Booking findById (String id);

}
