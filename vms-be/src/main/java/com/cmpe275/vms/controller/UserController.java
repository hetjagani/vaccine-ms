package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.security.CurrentUser;
import com.cmpe275.vms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/me")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<com.cmpe275.vms.model.User> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getUsername())));
    }
}
