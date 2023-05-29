package com.springsecuritydemo.service;


import com.springsecuritydemo.entity.User;
import com.springsecuritydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//import java.util.Collection;
//import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Optional<User> user = userRepo.getByMail(mail);
        return user.map(User::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + mail));

    }
}
