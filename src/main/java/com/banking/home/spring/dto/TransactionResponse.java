package com.banking.home.spring.dto;

import java.time.LocalDateTime;

public class TransactionResponse {
    private Long id;
    private String senderIban;
    private String receiverIban;
    private Double amount;
    private LocalDateTime timestamp;

    public TransactionResponse(Long id, String senderIban, String receiverIban, Double amount, LocalDateTime timestamp) {
        this.id = id;
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderIban() {
		return senderIban;
	}

	public void setSenderIban(String senderIban) {
		this.senderIban = senderIban;
	}

	public String getReceiverIban() {
		return receiverIban;
	}

	public void setReceiverIban(String receiverIban) {
		this.receiverIban = receiverIban;
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
}
