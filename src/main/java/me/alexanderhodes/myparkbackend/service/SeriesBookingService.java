package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.SeriesBooking;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeriesBookingService extends CrudRepository<SeriesBooking, String> {

    public List<SeriesBooking> findByUser (User user);

}
