package com.example.demo.repos;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    @PostConstruct
    private void addUserAndAdmin() {
        // password - Junko Enoshima
        var admin = new User("Junko Enoshima", "$2a$10$3R/wpWW8ST5jDEFznCZnKeWnWt6B8HOG/BdZVuAJoqFH32B4XHVvK");
        admin.setRole(Role.ADMIN);
        save(admin);
        // password - Mukuro Ikusaba
        var user = new User("Mukuro Ikusaba", "$2a$10$fOUnJNzgi2HUDB6EUzAHxuj230fFf9nE8nsMLFe2QBeQVWkuECccq");
        user.setRole(Role.USER);
        save(user);
    }
}
