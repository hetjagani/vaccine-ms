package com.cmpe275.vms.security.oauth2;

import com.cmpe275.vms.exception.OAuth2AuthenticationProcessingException;
import com.cmpe275.vms.model.AuthProvider;
import com.cmpe275.vms.model.Role;
import com.cmpe275.vms.model.User;
import com.cmpe275.vms.model.VerifyToken;
import com.cmpe275.vms.repository.UserRepository;
import com.cmpe275.vms.repository.VerifyTokenRepository;
import com.cmpe275.vms.security.UserPrincipal;
import com.cmpe275.vms.security.oauth2.user.OAuth2UserInfo;
import com.cmpe275.vms.security.oauth2.user.OAuth2UserInfoFactory;
import com.cmpe275.vms.util.MailUtil;
import com.cmpe275.vms.util.RandomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerifyTokenRepository verifyTokenRepository;

    @Value("${spring.verify.endpoint}")
    private String verifyEndpoint;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);

            // create VerifyToken entity
            String verificationToken = RandomTokenUtil.generateToken();
            VerifyToken verifyToken = new VerifyToken(user.getEmail(), verificationToken);
            VerifyToken createdToken = verifyTokenRepository.save(verifyToken);
            String emailText = MailUtil.getVerificationMail(createdToken, verifyEndpoint);
            String emailSubject = "Please verify your email address for VMS";

            try {
                MailUtil.sendMail(emailText, emailSubject, user.getEmail());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User(userRepository);

        Role role = Role.PATIENT;
        if(oAuth2UserInfo.getEmail().split("@")[1].equals("sjsu.edu")) {
            role = Role.ADMIN;
        }
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setRole(role);
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName(oAuth2UserInfo.getFirstName());
        existingUser.setLastName(oAuth2UserInfo.getLastName());
        return userRepository.save(existingUser);
    }

}
