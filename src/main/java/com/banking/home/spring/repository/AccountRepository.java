package com.banking.home.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
    Optional<Account> findByOwnerEmail(String email);
    
    Optional<Account> findByIban(String iban);
    
    boolean existsByIban(String iban);
    boolean existsByOwner(User owner);
}
