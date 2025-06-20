package com.banking.home.spring.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonManagedReference
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonManagedReference
    private Account receiver;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(name = "description", nullable = true)
    private String description;
    
    // costruttori
    public Transaction() {};
    
    public Transaction(Account sender, Account receiver, double amount, String description) {
    	this.sender = sender;
    	this.receiver = receiver;
    	this.amount = amount;
    	this.description = description;
    }

    // getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getSender() {
		return sender;
	}

	public void setSender(Account sender) {
		this.sender = sender;
	}

	public Account getReceiver() {
		return receiver;
	}

	public void setReceiver(Account receiver) {
		this.receiver = receiver;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
}
