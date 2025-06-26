package com.banking.home.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.home.spring.dto.TransactionRequest;
import com.banking.home.spring.dto.TransactionResponse;
import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.Transaction;
import com.banking.home.spring.security.DatabaseUserDetails;
import com.banking.home.spring.service.AccountService;
import com.banking.home.spring.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	@Autowired
	AccountService accountService;
	
	@PostMapping("/create")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest request) {
		try {
			Transaction newTr = transactionService.transfer(request);
			return ResponseEntity.ok(newTr);
		} catch(IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }
	
	@GetMapping("/account/{accountId}")
	public ResponseEntity<?> getTransactionHistory(
	        @PathVariable Long accountId,
	        @AuthenticationPrincipal DatabaseUserDetails currentUser) {
		
		try {
			Account account = accountService.getById(accountId);
			
			if (!account.getOwner().getId().equals(currentUser.getId())) {
		        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		    }

		    List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
		    List<TransactionResponse> responses = transactions.stream()
		            .map(tx -> new TransactionResponse(
		                    tx.getId(),
		                    tx.getSender().getIban(),
		                    tx.getReceiver().getIban(),
		                    tx.getAmount(),
		                    tx.getTimestamp()
		            ))
		            .toList();

		    return ResponseEntity.ok(responses);
		} catch(IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}

	    
	}

}
