package com.barbershop.exceptions;

import java.time.LocalDate;

import com.barbershop.entities.Shop;

public class ShopClosedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShopClosedException(Shop shop, LocalDate date) {
        super("A barbearia \"" + shop.getName() + "\" não abre em " + date.getDayOfWeek() + " (" + date + ")");
    }
}
