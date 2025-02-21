package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.dto.UserResponse;
import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.repository.UserRepository;
import com.redaeilco.ecommerce.service.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @GetMapping()
    public ResponseEntity<UserResponse> getUserByToken(@RequestHeader("Authorization") String token) {
        int userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).get();
        UserResponse userResponse = new UserResponse(user.getUsername(), user.getRole());
         return ResponseEntity.ok(userResponse);
    }
}
