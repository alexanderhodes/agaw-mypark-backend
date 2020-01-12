package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// added because of error:
// https://stackoverflow.com/questions/35454494/not-supported-for-dml-operations-unable-to-update-data-in-postgresql-database-u
@Transactional
public interface TokenService extends CrudRepository<Token, String> {

    @Modifying
    public void deleteByValidUntilBefore (LocalDateTime localDateTime);
}
