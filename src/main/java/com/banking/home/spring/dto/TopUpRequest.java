package com.banking.home.spring.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TopUpRequest {
	
	@NotBlank(message = "Numero della carta necessario")
	private String cardNumber;
	
	@NotNull(message = "L'importo della ricarica è necessario")
	@DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di 0")
	private Double amount;
	
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
