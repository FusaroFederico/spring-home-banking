package com.banking.home.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.home.spring.dto.AuthResponse;
import com.banking.home.spring.dto.LoginRequest;
import com.banking.home.spring.dto.RegisterRequest;
import com.banking.home.spring.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
