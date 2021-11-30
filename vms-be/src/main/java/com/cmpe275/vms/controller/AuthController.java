package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.UserAlreadyExistsException;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.payload.*;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.security.TokenProvider;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest user) throws UserAlreadyExistsException {
        User eg = new User();
        eg.setEmail(user.getEmail());
        Optional<User> optUser = userRepository.findOne(Example.of(eg));
        if(!optUser.isEmpty()) {
            throw new UserAlreadyExistsException("user already exists");
        }

        // TODO: if user has SJSU email then authority is ADMIN
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        // created user in database
        User dbUser = new User(userRepository, user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(),
                user.getDateOfBirth(), user.getGender(), user.getAddress(), user.getRole());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(dbUser);

        // generate token
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(createdUser.getEmail(), user.getPassword(), grantedAuthorities));

        SecurityContextHolder.getContext().setAuthentication(auth);
        final String token = tokenProvider.createToken(auth);

        return ResponseEntity.ok(new AuthResponse(token));

//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new BadRequestException("Email address already in use.");
//        }
//
//        // Creating user's account
//        User user = new User();
//        user.setName(signUpRequest.getName());
//        user.setEmail(signUpRequest.getEmail());
//        user.setPassword(signUpRequest.getPassword());
//        user.setProvider(AuthProvider.local);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        User result = userRepository.save(user);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/user/me")
//                .buildAndExpand(result.getId()).toUri();
//
//        return ResponseEntity.created(location)
//                .body(new ApiResponse(true, "User registered successfully@"));
    }

}
