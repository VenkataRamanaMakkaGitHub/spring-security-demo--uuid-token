package com.springsecuritydemo.repo;

import com.springsecuritydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
        Optional<User> getByMail(String mail);
        User getByToken(String token);
}
