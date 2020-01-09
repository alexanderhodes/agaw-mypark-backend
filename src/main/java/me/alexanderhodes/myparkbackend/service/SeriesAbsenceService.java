package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.SeriesAbsence;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeriesAbsenceService extends CrudRepository<SeriesAbsence, String> {

    public List<SeriesAbsence> findByUser (User user);

}
