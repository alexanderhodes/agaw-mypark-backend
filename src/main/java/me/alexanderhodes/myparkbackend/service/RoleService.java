package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleService extends CrudRepository<Role, String> {
}
