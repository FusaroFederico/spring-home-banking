package com.banking.home.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.home.spring.dto.UserInfoResponse;
import com.banking.home.spring.security.DatabaseUserDetails;
import com.banking.home.spring.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(401).build();
	    }
		
	    DatabaseUserDetails userDetails = (DatabaseUserDetails) authentication.getPrincipal();
	    
        return ResponseEntity.ok(new UserInfoResponse(
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getUsername()
        ));
    }

}
