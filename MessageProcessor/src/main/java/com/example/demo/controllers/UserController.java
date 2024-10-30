package com.example.demo.controllers;

import com.example.demo.DbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    private final DbClient dbClient;

    @Autowired
    public UserController(DbClient dbClient) {
        this.dbClient = dbClient;
    }

    @PostMapping("/{email}")
    public String addNewUser(@PathVariable("email") String email) {
        var id = dbClient.addUser(email);
        return id;
    }
}
