package com.banking.home.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.home.spring.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
