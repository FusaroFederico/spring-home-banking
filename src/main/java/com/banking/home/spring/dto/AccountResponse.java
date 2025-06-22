package com.banking.home.spring.dto;

public class AccountResponse {

	private Long id;
	private String iban;
	private Double balance;
	
	public AccountResponse(Long id, String iban, Double balance) {
		this.id = id;
        this.iban = iban;
        this.balance = balance;
    }
	
	
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
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
