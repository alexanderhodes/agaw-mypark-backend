package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserService extends CrudRepository<User, Long> {
}
