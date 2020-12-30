package com.kingland.lunch.learn.demo.lifecycle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.NonNull;
import com.github.javafaker.Faker;
import com.kingland.lunch.learn.demo.model.Person;
import com.kingland.lunch.learn.demo.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Log
@Component
@AllArgsConstructor
public class OnStartAction implements ApplicationListener<ApplicationStartedEvent> {

    private final @NonNull DatabaseClient client;

    private final @NonNull PersonRepository repo;

    private final @NonNull Faker faker;

    /**
     * Init test data on application started
     *
     * @param event application started event
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            log.info("Start Create Test Database");
            Path path = Path.of(ClassLoader.getSystemResource("db/migration/CreateDB.sql").getPath());
            String sql = Files.readString(path);
            client.sql(sql).then().subscribe();
            createTestData().subscribe();
            log.info("Test Database Created");
        } catch (IOException e) {
            log.throwing("OnStartAction", "onApplicationEvent", e);
        }
    }

    /**
     * Create test data
     */
    private Flux<Person> createTestData() {
        List<Person> testPeople = IntStream.range(1, 10).mapToObj(id -> {
            Person person = Person.builder()
                    .name(faker.name().firstName())
                    .age(faker.number().numberBetween(20, 40))
                    .gender(faker.number().numberBetween(0, 1)).build();
            person.setEmail(person.getName() + id + "@test.com");
            return person;
        }).collect(Collectors.toList());
        return repo.saveAll(testPeople).log();
    }
}
