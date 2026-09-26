package com.barbershop.entities;

import java.time.Duration;

public class ServiceProvided {
	private String name;
	private Duration minutesDuration;
	private double price;
	
	public ServiceProvided(String name, Duration minutesDuration, double price) {
		this.name = name;
		
		if(minutesDuration.isNegative() || minutesDuration.isZero()) {
			throw new IllegalArgumentException("A duração do serviço deve ser maior que 0.");
		}
		
		this.minutesDuration = minutesDuration;
		this.price = price;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Duration getMinutesDuration() {
		return minutesDuration;
	}

	public void setMinutesDuration(int minutesDuration) {
		this.minutesDuration = Duration.ofMinutes(minutesDuration);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
