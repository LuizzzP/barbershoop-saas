package com.barbershop.services;

import java.time.LocalDateTime;

import com.barbershop.entities.Appointment;
import com.barbershop.entities.ProfessionalUser;
import com.barbershop.entities.ServiceProvided;
import com.barbershop.entities.Shop;
import com.barbershop.enums.AppointmentStatus;

public class AppointmentService {
	
	
	
	
	public void makeAppointment() {
		
	}
	
	public boolean validateHour(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
		return !(end1.isBefore(start2) || end1.equals(start2) || 
                start1.isAfter(start2) || start1.equals(start2));
	}
}
