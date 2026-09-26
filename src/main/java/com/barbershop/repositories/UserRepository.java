package com.barbershop.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.barbershop.entities.User;

public class UserRepository {
	private List<User> users = new ArrayList<>();
	private Long nextId = 0L;
	
	
	public Long getNextId() {
		nextId++;
		return nextId;
	}
	
	public void saveUser(User user) {
		users.add(user);
	}
	
	public Optional<User> findByEmail(String email) {
		return users.stream()
				.filter(u -> u.getEmail().equalsIgnoreCase(email))
				.findFirst();
	}

	public boolean emailExists(String email) {
		return findByEmail(email).isPresent();
	}
}
