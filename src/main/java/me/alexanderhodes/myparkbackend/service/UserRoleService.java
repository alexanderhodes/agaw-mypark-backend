package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Role;
import me.alexanderhodes.myparkbackend.model.User;
import me.alexanderhodes.myparkbackend.model.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleService extends CrudRepository<UserRole, String> {

    public UserRole findByUserAndRole (User user, Role role);

    public List<UserRole> findByRole (Role role);

}
