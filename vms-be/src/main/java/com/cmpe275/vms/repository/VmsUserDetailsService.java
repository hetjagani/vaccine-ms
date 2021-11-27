package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class VmsUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setEmail(username);
        Optional<User> optUser = userRepository.findOne(Example.of(user));
        if(optUser.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(optUser.get().getRole().toString());
        return new org.springframework.security.core.userdetails.User(optUser.get().getEmail(), optUser.get().getPassword(), Arrays.asList(authority));
    }
}
