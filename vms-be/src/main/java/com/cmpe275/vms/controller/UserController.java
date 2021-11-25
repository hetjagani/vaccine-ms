package com.cmpe275.vms.controller;

import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.UserRequest;
import com.cmpe275.vms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userRepository.findAll();

        return ResponseEntity.ok(userList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return ResponseEntity.ok(user.orElseThrow());
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequest user) {
        try {
            System.out.println(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }
}
