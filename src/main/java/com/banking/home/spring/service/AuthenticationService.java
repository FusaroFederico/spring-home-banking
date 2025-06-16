package com.banking.home.spring.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.home.spring.dto.AuthResponse;
import com.banking.home.spring.dto.LoginRequest;
import com.banking.home.spring.dto.RegisterRequest;
import com.banking.home.spring.model.User;
import com.banking.home.spring.security.DatabaseUserDetails;
import com.banking.home.spring.security.JwtService;

@Service
public class AuthenticationService {

	private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationService(UserService userService,
    							 PasswordEncoder passwordEncoder,
    							 JwtService jwtService,
    							 AuthenticationManager authenticationManager) {
    	this.userService = userService;
    	this.passwordEncoder = passwordEncoder;
    	this.authenticationManager = authenticationManager;
    	this.jwtService = jwtService;
    }
    
    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

     // Recupera i dettagli dell'utente dal database
        UserDetails userDetails = userService.findByEmail(request.getEmail())
                .map(DatabaseUserDetails::new)
                .orElseThrow(() -> new RuntimeException("Utente non trovato."));
        
        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponse(jwt);
    }
    
    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        userService.saveUser(user);
    }
}
