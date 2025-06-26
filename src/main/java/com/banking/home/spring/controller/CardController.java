package com.banking.home.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.home.spring.dto.TopUpRequest;
import com.banking.home.spring.model.Card;
import com.banking.home.spring.model.CardType;
import com.banking.home.spring.security.DatabaseUserDetails;
import com.banking.home.spring.service.CardService;
import com.banking.home.spring.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cards")
public class CardController {
	
	 @Autowired
	 private CardService cardService;
	 
	 @Autowired
	 private UserService userService;
	 
	 @PostMapping("/create")
	 public ResponseEntity<?> createCard(@AuthenticationPrincipal DatabaseUserDetails currentUser, @RequestParam CardType type) {
		 try {
			 Card card = cardService.createCard(userService.findByEmail(currentUser.getUsername()).get(), type);
			 return ResponseEntity.ok(card);
		 } catch (Exception ex) {
			 return ResponseEntity.badRequest().body(ex.getMessage());
		 }
	 }
	
	 @GetMapping("/mycards")
	 public ResponseEntity<?> getCards(@AuthenticationPrincipal DatabaseUserDetails currentUser) {
		 try {
			 return ResponseEntity.ok(cardService.getCards(userService.findByEmail(currentUser.getUsername()).get()));
		 } catch (Exception ex) {
			 return ResponseEntity.badRequest().body(ex.getMessage());
		 }
	 }
	 
	 @PostMapping("/topup")
	 public ResponseEntity<?> topUpCard(@AuthenticationPrincipal DatabaseUserDetails currentUser,
			 							@Valid @RequestBody TopUpRequest request) {
	     try {
	         cardService.topUpPrepaidCard(userService.findByEmail(currentUser.getUsername()).get(), request.getCardNumber(), request.getAmount());
	         return ResponseEntity.ok("Ricarica effettuata con successo.");
	     } catch (Exception e) {
	         return ResponseEntity.badRequest().body(e.getMessage());
	     }
	 }

}
