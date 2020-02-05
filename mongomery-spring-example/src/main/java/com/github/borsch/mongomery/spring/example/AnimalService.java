package com.github.borsch.mongomery.spring.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    public void saveNewAnimal(final String name, final int age, final double weight) {
        final Animal animal = new Animal();
        animal.setName(name);
        animal.setAge(age);
        animal.setWeight(weight);

        animalRepository.save(animal);
    }

    public List<Animal> getAll() {
        return animalRepository.findAll();
    }

}
