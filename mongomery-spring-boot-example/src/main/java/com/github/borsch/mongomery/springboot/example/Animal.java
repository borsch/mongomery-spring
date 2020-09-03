package com.github.borsch.mongomery.springboot.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "animal")
public class Animal {

    @Id
    private String id;
    private String name;
    private int age;
    private double weight;
    private LocalDateTime localDateTime;
    private LocalDate localDate;

}
