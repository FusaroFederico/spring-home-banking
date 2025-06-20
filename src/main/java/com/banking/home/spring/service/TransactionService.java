package com.banking.home.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.home.spring.dto.TransactionRequest;
import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.Transaction;
import com.banking.home.spring.repository.AccountRepository;
import com.banking.home.spring.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository repo;
	
	@Autowired
	private AccountRepository accountRepo;
	
	public Transaction transfer(TransactionRequest request) {
		// controlla se esiste il conto mittente
		if (!accountRepo.existsByIban(request.getSenderIban())) {
			throw new IllegalArgumentException("Conto mittente non trovato.");
		}
		Account senderAccount = accountRepo.findByIban(request.getSenderIban()).get(); 
		// controlla se esiste il conto ricevente
		if(!accountRepo.existsByIban(request.getReceiverIban())) {
			throw new IllegalArgumentException("Conto destinatario non trovato.");
		}
		// recupera il conto ricevente
		Account reciever = accountRepo.findByIban(request.getReceiverIban()).get();
		// controlla l'importo da trasferire
		if(request.getAmount() <= 0) {
			throw new IllegalArgumentException("L'importo deve essere positivo.");
		}
		if(senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
			throw new IllegalArgumentException("Fondi insufficienti per effettuare l'operazione.");
		}
		// aggiorna i conti e salva 
		senderAccount.setBalance(senderAccount.getBalance() - request.getAmount());
		reciever.setBalance(reciever.getBalance() + request.getAmount());
		
		accountRepo.save(senderAccount);
		accountRepo.save(reciever);
		
		// crea e salva la transazione appena avvenuta
		Transaction transaction = new Transaction(senderAccount, reciever, request.getAmount(), request.getDescription());
		return repo.save(transaction);
	}
	
	public Transaction findById(Long id) {
		if (repo.existsById(id)) {
			return repo.findById(id).get();
		}
		throw new IllegalStateException("Transazione non trovata.");
	}
	
	public List<Transaction> getTransactionsForAccount(Long accountId) {
	    return repo.findBySenderIdOrReceiverId(accountId, accountId);
	}

}
