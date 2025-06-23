package com.banking.home.spring.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 27)
    private String iban;

    @Column(nullable = false)
    private Double balance;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User owner;
    
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Transaction> sendedTransactions;
    
    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Transaction> receivedTransactions;
    
    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Card> cards;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Transaction> getSendedTransactions() {
		return sendedTransactions;
	}

	public void setSendedTransactions(List<Transaction> sendedTransactions) {
		this.sendedTransactions = sendedTransactions;
	}

	public List<Transaction> getReceivedTransactions() {
		return receivedTransactions;
	}

	public void setReceivedTransactions(List<Transaction> receivedTransactions) {
		this.receivedTransactions = receivedTransactions;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
}
