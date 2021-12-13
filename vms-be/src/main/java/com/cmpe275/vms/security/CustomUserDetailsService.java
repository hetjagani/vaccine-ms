package com.cmpe275.vms.security;


import com.cmpe275.vms.model.User;
import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(email);
        if(optUser.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(optUser.get().getRole().toString());
        return new UserPrincipal(optUser.get().getMrn(), optUser.get().getEmail(), optUser.get().getPassword(), List.of(authority));
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}