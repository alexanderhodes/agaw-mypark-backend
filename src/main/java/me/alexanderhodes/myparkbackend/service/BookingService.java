package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService extends CrudRepository<Booking, Long> {

    public List<Booking> findByUserOrderByDateAsc (User user);

    public Booking findById (String id);

    public List<Booking> findByUserAndDateAfterAndDateBefore (User user, LocalDateTime after, LocalDateTime before);

    public void deleteById (String id);

}
