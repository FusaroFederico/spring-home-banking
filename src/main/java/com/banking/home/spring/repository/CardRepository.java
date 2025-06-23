package com.banking.home.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.home.spring.model.Account;
import com.banking.home.spring.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	boolean existsByCardNumber(String cardNumber);
    List<Card> findByAccount(Account account);
    Card findByCardNumber(String cardNumber);
}
