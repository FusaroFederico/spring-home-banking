package com.banking.home.spring.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.Card;
import com.banking.home.spring.model.CardType;
import com.banking.home.spring.model.User;
import com.banking.home.spring.repository.AccountRepository;
import com.banking.home.spring.repository.CardRepository;

@Service
public class CardService {
	
	@Autowired
    private AccountRepository accountRepo;

    @Autowired
    private CardRepository cardRepo;

    public Card createCard(User user, CardType type) {
    	// recupera il conto
        Account account = accountRepo.findByOwnerEmail(user.getEmail())
        		.orElseThrow(() -> new IllegalStateException("Conto non trovato."));
        // genera il numero della carta 
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (cardRepo.existsByCardNumber(cardNumber));
        // imposta la scadenza a 3 anni da adesso
        LocalDate expiration = LocalDate.now().plusYears(3);
        // se la carta è prepagata, imposta il saldo a 0.00
        Double balance = (type == CardType.PREPAID) ? 0.00 : null;

        Card card = new Card(type, cardNumber, expiration, balance, account);
        return cardRepo.save(card);
    }

    public List<Card> getCards(User user) {
    	Account account = accountRepo.findByOwnerEmail(user.getEmail())
        		.orElseThrow(() -> new IllegalStateException("Conto non trovato."));
        return cardRepo.findByAccount(account);
    }
    
    public void topUpPrepaidCard(User user, String cardNumber, Double amount) {
    	// controlla che la somma sia positiva
        if (amount <= 0) {
            throw new IllegalArgumentException("La somma da ricaricare deve essere positiva.");
        }
        // recupera la carta, se non la trova lancia un'eccezione
        Card card = cardRepo.findByCardNumber(cardNumber);
        if (card == null) {
        	throw new IllegalArgumentException("Carta non trovata.");
        }
        // solo il proprietario della carta può effettuare la ricarica
        if (!card.getAccount().getOwner().getId().equals(user.getId())) {
            throw new SecurityException("Operazione non autorizzata.");
        }
        // controlla il tipo di carta
        if (card.getType() != CardType.PREPAID) {
            throw new IllegalArgumentException("Solo le carte prepagate possono essere ricaricate.");
        }

        Account account = card.getAccount();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Fondi insufficienti sul conto.");
        }

        account.setBalance(account.getBalance() - amount);
        card.setBalance(card.getBalance() + amount);

        accountRepo.save(account);
        cardRepo.save(card);
    }

    // genera un numero di carta casuale a 16 cifre con il prefisso 4 (Visa)
    private String generateCardNumber() {
        Random rand = new Random();
        StringBuilder number = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            number.append(rand.nextInt(10));
        }
        return number.toString();
    }
}
