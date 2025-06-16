package com.banking.home.spring.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.home.spring.model.Role;
import com.banking.home.spring.model.User;
import com.banking.home.spring.repository.RoleRepository;
import com.banking.home.spring.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	public Optional<User> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public List<User> findAll(){
		return userRepo.findAll();
	}

	public User saveUser(User user) {
		// aggiunge il ruolo 'USER' 
		Set<Role> roles = new HashSet<Role>();
        roles.add(roleRepo.findByName("USER").get());
        user.setRoles(roles);
        
		return userRepo.save(user);
	}
}
