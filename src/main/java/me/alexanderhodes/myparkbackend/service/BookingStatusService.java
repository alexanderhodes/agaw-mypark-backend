package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.BookingStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingStatusService extends CrudRepository<BookingStatus, String> {

    public BookingStatus findByName (String name);

    public List<BookingStatus> findAll();

}
