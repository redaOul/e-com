package com.redaeilco.ecommerce.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

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

    public Map<String, Object> registerUser(User user) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Encrypt the password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        // Set the role
        if (user.getRole() == null) {
            user.setRole("user");
        }

        try {
            // Save the user to the database
            User userSaved = userRepository.save(user);
    
            // Generate a JWT token for the user
            String token = jwtService.generateToken(userSaved.getUsername(), userSaved.getRole(), userSaved.getId());
    
            // Return the username and token
            return new HashMap<>() {{
                put("username", userSaved.getUsername());
                put("token", token);
            }};
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username is already taken");
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public Map<String, Object> loginUser(User user) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())); 

            User fullUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

            // Generate a JWT token for the user
            String token = jwtService.generateToken(fullUser.getUsername(), fullUser.getRole(), fullUser.getId());
            
            // Return the username and token
            return new HashMap<>() {{
                put("username", fullUser.getUsername());
                put("token", token);
            }};
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }
}
