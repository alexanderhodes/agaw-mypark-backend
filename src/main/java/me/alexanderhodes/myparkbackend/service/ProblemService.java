package me.alexanderhodes.myparkbackend.service;

import me.alexanderhodes.myparkbackend.model.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemService extends CrudRepository<Problem, String> {

}
