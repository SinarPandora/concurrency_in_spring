package com.kingland.lunch.learn.demo.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import com.kingland.lunch.learn.demo.model.Person;
import com.kingland.lunch.learn.demo.repository.PersonRepository;
import com.kingland.lunch.learn.demo.service.AppService;
import com.kingland.lunch.learn.demo.service.DemoService;
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
    private final @NonNull AppService appService;
    private final @NonNull DemoService demoService;

    @GetMapping("/print_and_wait")
    public String printAndWait() throws InterruptedException {
        System.out.printf("Current Thread is %s%n", Thread.currentThread().getName());
        Thread.sleep(100_000);
        return "DONE";
    }

    @GetMapping("/listAll")
    public Flux<Person> listAllPeople() {
        return repo.findAll();
    }

    @GetMapping("/waiting")
    public String waiting() {
        appService.waiting();
        return "Finished";
    }

    @GetMapping("/cpu")
    public CompletableFuture<Long> calc() {
        appService.cpuCalcTask(45);
        appService.cpuCalcTask(45);
        return appService.cpuCalcTask(45);
    }

    @GetMapping("/io/1")
    public CompletableFuture<Integer> httpCall1() {
        return appService.batchHttpCall(3, 10);
    }

    @GetMapping("/io/2")
    public CompletableFuture<Integer> httpCall2() {
        return demoService.batchHttpCall(3, 10);
    }

    @GetMapping("/error")
    public void error() {
        appService.error();
    }
}
