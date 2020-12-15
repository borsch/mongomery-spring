package com.github.borsch.mongomery.spring.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.borsch.mongomery.spring.listeners.MongomeryExecutionListener;
import com.github.borsch.mongomery.spring.types.DatabaseMongoSetup;
import com.github.borsch.mongomery.spring.types.ExpectedMongoDatabase;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testContext.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, MongomeryExecutionListener.class })
public class AnimalServiceTest {

    @Autowired
    private AnimalService animalService;

    @Test
    @ExpectedMongoDatabase("/expectedAfterSave.json")
    public void shouldSaveNewAnimal() {
        animalService.saveNewAnimal("name", 10, 10.1);
    }

    @Test
    @DatabaseMongoSetup("/databaseSetup.json")
    public void shouldReadAllAnimals() {
        assertThat(animalService.getAll())
            .hasSize(1)
            .extracting(
                Animal::getName,
                Animal::getAge,
                Animal::getWeight
            )
            .containsExactly(
                tuple("storedAnimalName", 11, 17.3)
            );
    }

    @Configuration
    static class TestConfig {
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