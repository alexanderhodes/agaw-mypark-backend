package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserService extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
