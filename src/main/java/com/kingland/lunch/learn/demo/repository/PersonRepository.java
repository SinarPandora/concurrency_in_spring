package com.kingland.lunch.learn.demo.repository;

import com.kingland.lunch.learn.demo.model.Person;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonRepository extends ReactiveCrudRepository<Person, Long> {
}
