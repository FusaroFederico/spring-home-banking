package com.banking.home.spring.dto;

public class TransactionRequest {
	
	private String senderIban;
	private String receiverIban;
    private Double amount;
    private String description;
    
    
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
}
