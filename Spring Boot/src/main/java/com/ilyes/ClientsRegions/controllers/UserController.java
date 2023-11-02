package com.ilyes.ClientsRegions.controllers;

import com.ilyes.ClientsRegions.Errors.CustomErrorMessage;
import com.ilyes.ClientsRegions.entities.User;
import com.ilyes.ClientsRegions.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                userRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(existingUser);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("User not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage("Internal Server Error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                User existing = existingUser.get();
                existing.setLastname(user.getLastname());
                existing.setFirstname(user.getFirstname());
                existing.setEmail(user.getEmail());
                existing.setRole(user.getRole());


                userRepository.save(existing);

                return ResponseEntity.ok(existingUser);
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorMessage("User not found"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorMessage(e.getMessage()));
        }
    }
}
