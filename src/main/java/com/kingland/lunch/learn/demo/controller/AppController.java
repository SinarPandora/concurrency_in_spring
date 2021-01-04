package com.kingland.lunch.learn.demo.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import com.kingland.lunch.learn.demo.model.Person;
import com.kingland.lunch.learn.demo.repository.PersonRepository;
import com.kingland.lunch.learn.demo.service.AppService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Log
@AllArgsConstructor
@RestController
public class AppController {
    private final @NonNull PersonRepository repo;
    private final @NonNull AppService service;
    private final Semaphore semaphore = new Semaphore(1);

    @GetMapping("/print_and_wait")
    public String printAndWait() throws InterruptedException {
        System.out.printf("Current Thread is %s%n", Thread.currentThread().getName());
        Thread.sleep(100_000);
        return "DONE";
    }
    
    @GetMapping("/print_and_lock")
    public String printAndLock() throws InterruptedException {
        System.out.printf("Current Thread is %s%n", Thread.currentThread().getName());
        semaphore.acquire();
        semaphore.release();
        return "DONE";
    }
    
    @GetMapping("/lock")
    public String lockIt() throws InterruptedException {
        semaphore.acquire();
        return "Request has been locked, you can use /unlock to unlock it";
    }

    @GetMapping("/unlock")
    public String unlockIt() {
        semaphore.release();
        return "Request Unlocked";
    }
    
    @GetMapping("/listAll")
    public Flux<Person> listAllPeople() {
        return repo.findAll();
    }
    
    @GetMapping("/waiting")
    public String waiting() {
        service.waiting();
        return "Finished";
    }
    
    @GetMapping("/cpu")
    public CompletableFuture<Long> calc() {
        service.cpuCalcTask(45);
        service.cpuCalcTask(45);
        return service.cpuCalcTask(45);
    }
    
    @GetMapping("/io")
    public CompletableFuture<Integer> httpCall() {
        return service.batchHttpCall(3, 10);
    }
}
