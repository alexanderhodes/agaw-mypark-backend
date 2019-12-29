package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenService extends CrudRepository<Token, String> {
}
