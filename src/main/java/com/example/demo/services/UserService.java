package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final DbService dbService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DbService dbService, PasswordEncoder passwordEncoder) {
        this.dbService = dbService;
        this.passwordEncoder = passwordEncoder;
    }

    public String addUser(User user) {
        var dbUser = dbService.getUser(user.getEmail());
        if (dbUser != null) {
            return null;
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return dbService.addUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = dbService.getUser(name);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRoleToAuthority(user.getRole()));
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthority(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
