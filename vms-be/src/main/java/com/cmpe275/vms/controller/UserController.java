package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.VerifyToken;
import com.cmpe275.vms.payload.UserRequest;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.security.CurrentUser;
import com.cmpe275.vms.security.UserPrincipal;
import com.cmpe275.vms.util.MailUtil;
import com.cmpe275.vms.util.RandomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('ADMIN')")
    public ResponseEntity<com.cmpe275.vms.model.User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getUsername())));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('PATIENT') or hasAuthority('ADMIN')")
    @Transactional
    public ResponseEntity<com.cmpe275.vms.model.User> updateCurrentUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody UserRequest user) {
        User loggedInUser = userRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "email", userPrincipal.getUsername()));

        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());
        loggedInUser.setMiddleName(user.getMiddleName());
        loggedInUser.setAddress(user.getAddress());
        loggedInUser.setDateOfBirth(user.getDateOfBirth());
        loggedInUser.setGender(user.getGender());

        User updatedUser = userRepository.save(loggedInUser);

        return ResponseEntity.ok(updatedUser);
    }
}
