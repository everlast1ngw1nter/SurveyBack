package com.example.demo.controllers;

import com.example.demo.DbService;
import com.example.demo.dto.UserDto;
import com.example.demo.services.CheckingAvailabilityService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final CheckingAvailabilityService checkingAvailabilityService;

    @Autowired
    public UserController(UserService userService, CheckingAvailabilityService checkingAvailabilityService) {
        this.userService = userService;
        this.checkingAvailabilityService = checkingAvailabilityService;
    }

    @PostMapping("/registration")
    public String registrationUser(@RequestBody UserDto user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserDto user) {
        return userService.loginUser(user);
    }
}
