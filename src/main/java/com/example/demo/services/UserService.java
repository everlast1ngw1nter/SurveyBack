package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.UserDto;
import com.example.demo.models.Role;
import com.example.demo.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService{

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
            return null;
        }
        if(!passwordEncoder.matches(user.password(), currUser.getPassword())){
            return null;
        }
        return JwtGeneratorService.generateToken(user.email());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var currUser = dbService.getUser(username);
        return new org.springframework.security.core.userdetails.User(
                currUser.getEmail(),
                currUser.getPassword(),
                mapRoles(currUser)
        );
    }

    private Collection<GrantedAuthority> mapRoles(User appUser)
    {
        List<GrantedAuthority> collectionRoles = new ArrayList<>();
        collectionRoles.add(new SimpleGrantedAuthority("ROLE_" + appUser.getRole()));
        return collectionRoles;
    }
}
