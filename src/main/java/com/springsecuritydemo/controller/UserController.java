package com.springsecuritydemo.controller;
import com.springsecuritydemo.dto.LoginDto;
import com.springsecuritydemo.dto.LoginResponse;
import com.springsecuritydemo.entity.User;
import com.springsecuritydemo.dto.UserDto;
import com.springsecuritydemo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String welcome() {
        return "Hai..!";
    }

    @PostMapping("/signup")
    public String addUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setMail(userDto.getMail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()).toString());
        user.setToken(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepo.save(user);
        return "Done";
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody LoginDto loginDto) {
        Optional<User> optionalUser = userRepo.getByMail(loginDto.getMail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found..!");
        }
        User user=optionalUser.get();
        if (loginDto.getMail().equals(user.getMail())) {
            LoginResponse response = new LoginResponse();
            response.setUsername(loginDto.getMail());
            String newToken = UUID.randomUUID().toString();
            response.setAccessToken(newToken);
            user.setToken(newToken);
            userRepo.save(user);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Input");
        }

    }

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found With Id : " + id);
        } else {
            return ResponseEntity.ok(optionalUser.get());
        }

    }

}
