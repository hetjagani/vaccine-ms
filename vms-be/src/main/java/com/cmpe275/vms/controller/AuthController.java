package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.UserAlreadyExistsException;
import com.cmpe275.vms.model.Role;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.VerifyToken;
import com.cmpe275.vms.payload.*;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VerifyTokenRepository;
import com.cmpe275.vms.security.TokenProvider;
import com.cmpe275.vms.util.MailUtil;
import com.cmpe275.vms.util.RandomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.mail.MessagingException;
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

    @Autowired
    private VerifyTokenRepository verifyTokenRepository;

    @Value("${spring.verify.endpoint}")
    private String verifyEndpoint;

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

        Role role = Role.PATIENT;
        if(user.getEmail().split("@")[1].equals("sjsu.edu")) {
            role = Role.ADMIN;
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));

        // created user in database
        User dbUser = new User(userRepository, user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getDateOfBirth(), user.getGender(), user.getAddress(), role);
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(dbUser);

        // create VerifyToken entity
        String verificationToken = RandomTokenUtil.generateToken();
        VerifyToken verifyToken = new VerifyToken(createdUser.getEmail(), verificationToken);
        VerifyToken createdToken = verifyTokenRepository.save(verifyToken);
        String emailText = MailUtil.getVerificationMail(createdToken, verifyEndpoint);
        String emailSubject = "Please verify your email address for VMS";

        try {
            MailUtil.sendMail(emailText, emailSubject, createdUser.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // generate token
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(createdUser.getEmail(), user.getPassword(), grantedAuthorities));

        SecurityContextHolder.getContext().setAuthentication(auth);
        final String token = tokenProvider.createToken(auth);

        return ResponseEntity.ok(new AuthResponse(token));
    }

}
