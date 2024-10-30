package com.example.demo.controllers;

import com.example.demo.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    private final DbService dbService;

    @Autowired
    public UserController(DbService dbService) {
        this.dbService = dbService;
    }

    @PostMapping("/{email}")
    public String addNewUser(@PathVariable("email") String email) {
        var id = dbService.addUser(email);
        return id;
    }
}
