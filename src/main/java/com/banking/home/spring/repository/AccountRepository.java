package com.banking.home.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.home.spring.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerEmail(String email);
}
