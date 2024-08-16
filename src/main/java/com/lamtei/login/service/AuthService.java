package com.lamtei.login.service;

import com.lamtei.login.config.SecurityConfig;
import com.lamtei.login.constants.Constants;
import com.lamtei.login.entity.User;
import com.lamtei.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public String registerUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = securityConfig.passwordEncoder();
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return Constants.USERNAME_TAKEN;
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Constants.USER_REGISTERED;
    }

    public String loginUser(String username, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = securityConfig.passwordEncoder();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return Constants.LOGIN_SUCCESS;
            }
        }
        return Constants.INVALID_CREDENTIALS;
    }
}
