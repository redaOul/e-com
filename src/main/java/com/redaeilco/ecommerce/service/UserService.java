package com.redaeilco.ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.UserRepository;

/*
 * Service class for user-related operations such as registration, authentication, etc.
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(5);

    public Map<String, Object> registerUser(User user, String role) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return null;
        }

        // Encrypt the password and set the role
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(role);

        // Save the user to the database
        userRepository.save(user);

        // Generate a JWT token for the user
        String token = jwtService.generateToken(user.getUsername(), role, user.getId());

        // Return the username and token
        return new HashMap<>() {{
            put("username", user.getUsername());
            put("token", token);
        }};
    }

    public Map<String, Object> registerUser(User user) {
        return registerUser(user, "user");
    }
    
    public Map<String, Object> loginUser(User user) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())); 

        // If the user is not authenticated, return null
        if (!authentication.isAuthenticated()) {
            return null;
        }

        // Generate a JWT token for the user
        String token = jwtService.generateToken(user.getUsername(), user.getRole(), user.getId());

        // Return the username and token
        return new HashMap<>() {{
            put("username", user.getUsername());
            put("token", token);
        }};
    }
}
