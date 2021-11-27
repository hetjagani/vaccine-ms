package com.cmpe275.vms.controller;

import com.cmpe275.vms.errors.UserAlreadyExists;
import com.cmpe275.vms.filter.JwtFilter;
import com.cmpe275.vms.model.*;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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
    public ResponseEntity createUser(@RequestBody UserRequest user) throws UserAlreadyExists {

        User eg = new User();
        eg.setEmail(user.getEmail());
        Optional<User> optUser = userRepository.findOne(Example.of(eg));
        if(!optUser.isEmpty()) {
            throw new UserAlreadyExists("user already exists");
        }

        // TODO: if user has SJSU email then authority is ADMIN
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));


        // created user in database
        User dbUser = new User(user.getMrn(), user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(),
                user.getDateOfBirth(), user.getGender(), user.getAddress(), user.getRole());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(dbUser);

        // generate token
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(createdUser.getEmail(), user.getPassword(), grantedAuthorities));

        SecurityContextHolder.getContext().setAuthentication(auth);
        final String token = jwtUtil.generateToken(auth);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
