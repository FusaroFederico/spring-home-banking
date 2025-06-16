package com.banking.home.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.home.spring.model.Role;


public interface RoleRepository extends JpaRepository<Role , Integer> {
	
	public Optional<Role> findByName(String roleName);

}