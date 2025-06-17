package com.banking.home.spring.dto;

public class AccountResponse {

	private String iban;
	private Double balance;
	
	public AccountResponse(String iban, Double balance) {
        this.iban = iban;
        this.balance = balance;
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
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
