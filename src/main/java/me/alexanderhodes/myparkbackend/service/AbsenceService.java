package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Absence;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface AbsenceService extends CrudRepository<Absence, String> {

    public List<Absence> findByUser (User user);

    @Modifying
    public void deleteByEndBefore (LocalDate localDate);

}
