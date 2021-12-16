package com.cmpe275.vms.controller;

import com.cmpe275.vms.exception.ResourceNotFoundException;
import com.cmpe275.vms.model.AuthProvider;
import com.cmpe275.vms.model.Role;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.VerifyToken;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VerifyTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@RequestMapping(path = "/verifyEmail")
public class VerifyEmailController {

    @Autowired
    private VerifyTokenRepository verifyTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.verify.redirect}")
    private String verifyRedirect;

    @GetMapping
    public ModelAndView verifyEmail(@RequestParam String email, @RequestParam String token) {
        VerifyToken vt = new VerifyToken(email, token);
        Optional<VerifyToken> ot = verifyTokenRepository.findOne(Example.of(vt));
        if(ot.isEmpty()) {
            throw new BadCredentialsException("token invalid");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user", "email", email));

        user.setVerified(true);
        userRepository.save(user);


        return new ModelAndView("redirect:"+verifyRedirect);
    }
}
