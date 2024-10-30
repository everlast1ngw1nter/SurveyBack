package org.example.controllers;

import org.example.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final DbService dbService;

    @Autowired
    public UserController(DbService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/user/{email}")
    public String postNewUser(@PathVariable("email") String email) {
        var id = dbService.addUser(email);
        return id;
    }
}
