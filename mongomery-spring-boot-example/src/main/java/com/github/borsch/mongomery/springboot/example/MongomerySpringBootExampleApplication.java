package com.github.borsch.mongomery.springboot.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackageClasses = MongomerySpringBootExampleApplication.class)
@EnableMongoRepositories(basePackageClasses = AnimalRepository.class)
public class MongomerySpringBootExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongomerySpringBootExampleApplication.class, args);
    }

}
