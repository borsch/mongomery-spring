package com.github.borsch.mongomery.springboot.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.borsch.mongomery.spring.listeners.MongomeryExecutionListener;
import com.github.borsch.mongomery.spring.types.DatabaseMongoSetup;
import com.github.borsch.mongomery.spring.types.ExpectedMongoDatabase;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

@SpringBootTest
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, MongomeryExecutionListener.class })
class AnimalServiceTest {

    @Autowired
    private AnimalService service;

    @Test
    @ExpectedMongoDatabase("expectedAfterSave.json")
    void shouldSaveNewAnimal() {
        service.saveNewAnimal("name", 10, 10.1);
    }

    @Test
    @DatabaseMongoSetup("databaseSetup.json")
    void shouldReadAllAnimals() {
        assertThat(service.getAll())
            .extracting(
                Animal::getName,
                Animal::getAge,
                Animal::getWeight,
                Animal::getLocalDateTime,
                Animal::getLocalDate
            )
            .containsExactly(
                tuple("storedAnimalName", 11, 17.3, LocalDateTime.of(2020, 3, 4, 5, 6, 7), LocalDate.of(2222, 3, 6)),
                tuple(null, 11, 17.3, null, LocalDate.of(2222, 3, 6)),
                tuple("242", 11, 221.3, LocalDateTime.of(2021, 3, 4, 5, 6, 7), LocalDate.of(2222, 3, 6))
            );
    }

    @TestConfiguration
    static class TestConfig {

        @PostConstruct
        void started() {
            // important for valid LocalDateTime & LocalDate conversion
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        }

        @Bean
        MongoClient mongoClient() {
            try {
                MongodForTestsFactory factory = MongodForTestsFactory.with(Version.Main.V3_3);
                return factory.newMongo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}