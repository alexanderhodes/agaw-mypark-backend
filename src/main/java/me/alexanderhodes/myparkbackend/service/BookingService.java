package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Booking;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface BookingService extends CrudRepository<Booking, String> {

    public List<Booking> findByUserOrderByDateAsc (User user);

    public List<Booking> findByUserAndDateAfterAndDateBefore (User user, LocalDateTime after, LocalDateTime before);

    public void deleteById (String id);

    @Modifying
    public void deleteByDateBefore (LocalDateTime localDateTime);

}
