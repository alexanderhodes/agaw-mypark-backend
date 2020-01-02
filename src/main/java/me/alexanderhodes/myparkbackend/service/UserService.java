package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserService extends CrudRepository<User, Long> {

    public User findByUsername(String username);

    public User findById (String id);

    public List<User> findAllByEnabled (boolean enabled);

    public void deleteById (String id);

}
