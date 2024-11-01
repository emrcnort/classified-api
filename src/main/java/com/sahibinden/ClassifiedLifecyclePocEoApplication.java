package com.sahibinden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClassifiedLifecyclePocEoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClassifiedLifecyclePocEoApplication.class, args);
    }
}