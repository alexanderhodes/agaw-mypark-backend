package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.helper.UserAdmin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserService extends CrudRepository<User, String> {

    public User findByUsername(String username);

    public List<User> findAllByEnabled (boolean enabled);

    public void deleteById (String id);

    public User findByParkingSpace (ParkingSpace parkingSpace);

    public List<User> findByParkingSpaceIsNotNull ();

    public List<User> findByParkingSpaceIsNull ();

    @Query("select new me.alexanderhodes.myparkbackend.model.helper.UserAdmin(u, " +
            "case when count(u) > 1 then true else false end) from User u " +
            "join UserRole ur on u.id = ur.user.id " +
            "group by u")
    public List<UserAdmin> findByAdmin();

}
