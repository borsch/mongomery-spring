package com.github.borsch.mongomery.springboot.example;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalRepository extends MongoRepository<Animal, String> {

}
