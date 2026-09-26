package com.barbershop.services;

import java.util.List;

import com.barbershop.entities.ProfessionalUser;
import com.barbershop.entities.Shop;
import com.barbershop.exceptions.BarbershopDoNotFound;
import com.barbershop.repositories.BarbershopRepository;

public class BarbershopService {
	private BarbershopRepository repository = new BarbershopRepository();
	
	
	public Shop registerBarbershop(String name, ProfessionalUser user) {
		long id = repository.getNextId();
		Shop shop = new Shop(id, name);
		repository.saveShop(shop);
		user.addShop(shop);
		return shop;
	}
	
	public List<Shop> getBarbershopsList() {
        return repository.getShopList();
    }

}
