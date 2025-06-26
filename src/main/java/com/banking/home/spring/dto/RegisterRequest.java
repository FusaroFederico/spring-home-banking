package com.banking.home.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
	
	@Email(message = "Formato email non valido")
	@NotBlank(message = "Email necessaria")
	private String email;
	
	@NotBlank(message = "La password è necessaria")
	@Size(min = 6, message = "Password deve avere almeno 6 caratteri")
    private String password;
	
	@NotBlank(message = "Il nome è necessario")
    private String firstName;
	
	@NotBlank(message = "Il cognome è necessario")
    private String lastName;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    
}
