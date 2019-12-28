package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleService extends CrudRepository<UserRole, Long> {
}
