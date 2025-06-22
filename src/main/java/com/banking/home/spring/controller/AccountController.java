package com.banking.home.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.home.spring.dto.AccountResponse;
import com.banking.home.spring.model.Account;
import com.banking.home.spring.security.DatabaseUserDetails;
import com.banking.home.spring.service.AccountService;
import com.banking.home.spring.service.UserService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
    public ResponseEntity<?> createAccount(@AuthenticationPrincipal DatabaseUserDetails currentUser) {
        try {
            Account newAccount = accountService.createAccountForUser(userService.findByEmail(currentUser.getUsername()).get());
            return ResponseEntity.ok(newAccount);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	@GetMapping("/me")
	public ResponseEntity<?> getMyAccount(@AuthenticationPrincipal DatabaseUserDetails currentUser) {
	    try {
	        Account account = accountService.getAccountByUser(userService.findByEmail(currentUser.getUsername()).get());
	        AccountResponse response = new AccountResponse(account.getId(), account.getIban(), account.getBalance());
	        return ResponseEntity.ok(response);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
}
