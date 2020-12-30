package com.kingland.lunch.learn.demo.service;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

@Log
@AllArgsConstructor
@Service
public class PersonService {
    private final @NonNull DatabaseClient client;
    
    @SneakyThrows
    public boolean waiting() {
        log.info("Start waiting");
        Thread.sleep(10000);
        log.info("Finish waiting");
        return true;
    }
}
