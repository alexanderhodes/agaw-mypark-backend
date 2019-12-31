package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.BookingStatus;
import org.springframework.data.repository.CrudRepository;

public interface BookingStatusService extends CrudRepository<BookingStatus, String> {

    public BookingStatus findByName (String name);

}
