package com.banking.home.spring.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TransactionRequest {
	
	@NotBlank(message = "L'IBAN del mittente è necessario")
	private String senderIban;
	
	@NotBlank(message = "L'IBAN del destinatario è necessario")
	private String receiverIban;
	
	@NotNull
	@DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di 0")
    private Double amount;
	
	@Size(max = 100, message = "La descrizione deve avere al massimo 100 caratteri")
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
