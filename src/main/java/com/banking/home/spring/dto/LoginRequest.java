package com.banking.home.spring.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
	
	@Email(message = "Formato email non valido")
	@NotBlank(message = "Email necessaria")
	private String email;
	
	@NotBlank(message = "Password necessaria")
    private String password;
    
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
}
