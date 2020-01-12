package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.SeriesBooking;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriesBookingService extends CrudRepository<SeriesBooking, String> {

    public List<SeriesBooking> findByUser (User user);

    @Query("select a from SeriesBooking a where a.active = true and a.weekday = :weekDay and " +
            "a.user.parkingSpace is null")
    public List<SeriesBooking> findActiveByWeekDay(@Param("weekDay") int weekDay);

}
