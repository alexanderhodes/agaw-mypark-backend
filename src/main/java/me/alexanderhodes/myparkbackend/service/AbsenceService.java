package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Absence;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AbsenceService extends CrudRepository<Absence, String> {

    public List<Absence> findByUser (User user);

}
