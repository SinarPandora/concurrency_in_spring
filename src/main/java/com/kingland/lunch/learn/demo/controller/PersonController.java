package com.kingland.lunch.learn.demo.controller;

import com.kingland.lunch.learn.demo.model.Person;
import com.kingland.lunch.learn.demo.repository.PersonRepository;
import com.kingland.lunch.learn.demo.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Log
@AllArgsConstructor
@RestController
public class PersonController {
    private final @NonNull PersonRepository repo;
    private final @NonNull PersonService service;

    @GetMapping("/listAll")
    public Flux<Person> listAllPeople() {
        return repo.findAll();
    }
    
    @GetMapping("/waiting")
    public String waiting() {
        service.waiting();
        return "Finished";
    }
}
