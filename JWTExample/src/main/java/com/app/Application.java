package com.app; // Adjust this package as needed

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // This enables component scanning in the package and its sub-packages
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
