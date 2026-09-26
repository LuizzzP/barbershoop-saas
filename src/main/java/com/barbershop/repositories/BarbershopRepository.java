package com.barbershop.repositories;

import java.util.ArrayList;
import java.util.List;

import com.barbershop.entities.Shop;
import com.barbershop.entities.User;

public class BarbershopRepository {
	private List<Shop> shopList = new ArrayList<>();
	private long nextId = 0L;
	
	public Long getNextId() {
		nextId++;
		return nextId;
	}
	
	public void saveShop(Shop shop) {
		shopList.add(shop);
	}
	
	public List<Shop> getShopList() {
		return shopList;
	}
}
