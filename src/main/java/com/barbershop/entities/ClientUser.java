package com.barbershop.entities;

import com.barbershop.enums.ClientType;

public class ClientUser extends User{
	public ClientUser() {}
	
	
	
	public ClientUser(long id, String email, String name, String passwordHash, ClientType type) {
		super(id, email, name, passwordHash, type);
	}
	
	
	
}
