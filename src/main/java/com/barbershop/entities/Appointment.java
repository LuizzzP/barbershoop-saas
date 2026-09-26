package com.barbershop.entities;


import java.time.LocalDateTime;

import com.barbershop.enums.AppointmentStatus;

public class Appointment {
	private AppointmentStatus status;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private ProfessionalUser professional;
	private ClientUser client = null;
	private ServiceProvided service;
	private Shop shop;
	
	public Appointment(AppointmentStatus status, LocalDateTime startTime, LocalDateTime endTime, ClientUser client, Shop shop, ServiceProvided service) {
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.client = client;
		this.shop = shop;
		this.service = service;
	}

	

	public AppointmentStatus getStatus() {
		return status;
	}
	
	public void confirmAppointment() {
		if(getStatus() != AppointmentStatus.PENDING) {
			throw new IllegalStateException("Só é possivel confirmar um agendamento pendente.");
		}
		
		this.setStatus(AppointmentStatus.CONFIRMED);
	}
	
	public void cancelAppointment() {
		if(getStatus() == AppointmentStatus.CANCELLED) {
			throw new IllegalStateException("Esse agendamento já está cancelado.");
		}
		
		this.setStatus(AppointmentStatus.CANCELLED);
	}
	
	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public ProfessionalUser getProfessional() {
		return professional;
	}

	public void setProfessional(ProfessionalUser professional) {
		this.professional = professional;
	}

	public ClientUser getClient() {
		return client;
	}

	public void setClient(ClientUser client) {
		this.client = client;
	}

	public ServiceProvided getService() {
		return service;
	}

	public void setService(ServiceProvided service) {
		this.service = service;
	}
	
	public Shop getShop() {
		return shop;
	}
	
	
}
