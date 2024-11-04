package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.Security.JwtGenerator;
import com.example.demo.dto.UserDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final DbService dbService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DbService dbService, PasswordEncoder passwordEncoder) {
        this.dbService = dbService;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserDto user){
        var userInDb = new User();
        if (dbService.getUser(user.email()) != null) {
            return "Пользователь с таким email уже существует";
        }
        userInDb.setEmail(user.email());
        var hashedPassword = passwordEncoder.encode(user.password());
        userInDb.setPassword(hashedPassword);
        userInDb.setRole(Role.USER);
        var idUser = dbService.addUser(userInDb);

        return "Пользователь успешно зарегистрирован " + idUser;
    }

    public String loginUser(UserDto user){
        var currUser = dbService.getUser(user.email());
        if (currUser == null) {
            return "Пользователь с таким email не существует";
        }
        if(!passwordEncoder.matches(user.password(), currUser.getPassword())){
            return "Введите правильный пароль";
        }
        return JwtGenerator.generateToken(user.email(), currUser.getPassword());
    }

}
