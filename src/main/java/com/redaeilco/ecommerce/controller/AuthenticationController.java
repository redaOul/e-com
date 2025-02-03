package com.redaeilco.ecommerce.controller;

import com.redaeilco.ecommerce.model.User;
import com.redaeilco.ecommerce.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User requestUser) {
        Map<String, Object> respond = userService.registerUser(requestUser);
        return ResponseEntity.ok(respond);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User requestUser) {
        Map<String, Object> respond = userService.loginUser(requestUser);
        return ResponseEntity.ok(respond);
    }

    @GetMapping("/hello")
    public String greeting() {
        return "hello again !";
    }
}
