package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.ParkingSpace;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.helper.UserAdmin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService extends CrudRepository<User, String> {

    @Query("select u from User u where u.username = :username or u.name = :username")
    public User findByUsernameOrName(@Param("username") String username);

    public List<User> findAllByEnabled (boolean enabled);

    public void deleteById (String id);

    public User findByParkingSpace (ParkingSpace parkingSpace);

    public List<User> findByParkingSpaceIsNotNull ();

    public List<User> findByParkingSpaceIsNull ();

    @Query("select new me.alexanderhodes.myparkbackend.model.helper.UserAdmin(u, " +
            "case when count(u) > 1 then true else false end) from User u " +
            "join UserRole ur on u.id = ur.user.id " +
            "where ur.role.name <> 'SYSTEM'" +
            "group by u")
    public List<UserAdmin> findByAdmin();

    public List<User> findAll();

}
