package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.app.entities.User;
import org.springframework.stereotype.Service; // Import the Service annotation

@Service // Annotate the class with @Service
public class UserService {
    private List<User> store = new ArrayList<>();

    public UserService() {
        store.add(new User(UUID.randomUUID().toString(), "Shubham", "shubham@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(), "Sham", "sham@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(), "Saurav", "saurav@gmail.com"));
        store.add(new User(UUID.randomUUID().toString(), "Shami", "shami@gmail.com"));
    }

    public List<User> getUsers() {
        return this.store;
    }
}
