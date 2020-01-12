package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.SeriesAbsence;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriesAbsenceService extends CrudRepository<SeriesAbsence, String> {

    public List<SeriesAbsence> findByUser (User user);

    @Query("select a from SeriesAbsence a where a.active = true and a.weekday = :weekDay and " +
            "a.user.parkingSpace is not null")
    public List<SeriesAbsence> findActiveByWeekDay(@Param("weekDay") int weekDay);

}
