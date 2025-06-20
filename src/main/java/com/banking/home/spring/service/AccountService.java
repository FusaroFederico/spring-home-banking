package com.banking.home.spring.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.User;
import com.banking.home.spring.repository.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
    private AccountRepository accountRepo;

    public Account createAccountForUser(User user) {
        if (accountRepo.existsByOwner(user)) {
            throw new IllegalStateException("L'utente ha già un conto.");
        }

        String iban;
        do {
            iban = generateRandomIban();
        } while (accountRepo.existsByIban(iban));

        Account account = new Account();
        account.setIban(iban);
        account.setOwner(user);
        account.setBalance(0.0);
        return accountRepo.save(account);
    }
    
    public Account findById(Long id) {
    	if (accountRepo.existsById(id)) {
    		return accountRepo.findById(id).get();
    	}
    	throw new IllegalArgumentException("Conto non trovato.");
    }
    
    public Account getAccountByIban(String iban) {
    	if (accountRepo.existsByIban(iban)) {
    		return accountRepo.findByIban(iban).get();
    	}
    	throw new IllegalArgumentException("Nessun conto trovato con questo iban.");
    }

    public Account getAccountByUser(User user) {
        return accountRepo.findByOwnerEmail(user.getEmail())
            .orElseThrow(() -> new IllegalStateException("Nessun conto trovato."));
    }
    
    // metodo per generare un iban fittizio su modello italiano
    private String generateRandomIban() {
        String countryCode = "IT"; // codice nazione
        String checkDigits = "60"; // codice di controllo
        String cin = "X";          // CIN fisso per semplicità 
        String abi = "12345"; // codice della banca
        String cab = String.format("%05d", new Random().nextInt(90000) + 10000); // codice della filiale
        String accountNumber = String.format("%012d", Math.abs(new Random().nextLong()) % 1_000_000_000_000L); // numero conto
        return countryCode + checkDigits + cin + abi + cab + accountNumber;
    }

}
