package com.barbershop.entities;

import java.util.ArrayList;
import java.util.List;

import com.barbershop.enums.ClientType;

public abstract class User {
	private Long id;
	private String email;
	private String name;
	private String passwordHash;
	private final ClientType type;
	private List<Appointment> appointments = new ArrayList<>();
	
	public User() {
		this.type = null;
	}

	public User(long id, String email, String name, String passwordHash, ClientType type) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.passwordHash = passwordHash;
		this.type = type;
	}
	
	public ClientType getClientType() {
		return type;
	}

	public Long getId() {
		return id;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void addAppointments(Appointment appointment) {
		appointments.add(appointment);
	}
	
	public void setPassword(String purePassword) {
		this.passwordHash = purePassword;
	}
	
	public boolean checkPassword(String password) {
		if(passwordHash.equals(password)) return true;
		else return false;
	}
	
}
