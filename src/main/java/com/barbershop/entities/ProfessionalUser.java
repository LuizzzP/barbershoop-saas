package com.barbershop.entities;

import java.util.ArrayList;
import java.util.List;

import com.barbershop.enums.ClientType;

public class ProfessionalUser extends User{
	private List<Shop> listShops = new ArrayList<>();

	public ProfessionalUser() {
		super();
	}

	public ProfessionalUser(long id, String email, String name, String passwordHash, ClientType type) {
		super(id, email, name, passwordHash, type);
	}
	
	public void addShop(Shop shop) {
		listShops.add(shop);
	}
	
	public List<Shop> getListShop() {
		return listShops;
	}
	
}
