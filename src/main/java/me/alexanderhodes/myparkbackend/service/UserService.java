package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserService extends CrudRepository<User, String> {

    public User findByUsername(String username);

    public List<User> findAllByEnabled (boolean enabled);

    public void deleteById (String id);

    public User findByParkingSpace (ParkingSpace parkingSpace);

    public List<User> findByParkingSpaceIsNotNull ();

}
