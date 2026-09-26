package com.barbershop.exceptions;

import com.barbershop.entities.Appointment;
import com.barbershop.entities.Shop;

public class AppointmentConflictException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppointmentConflictException(Shop shop, Appointment appointment) {
        super("Já existe um agendamento conflitante em \"" + shop.getName()
            + "\" no horário " + appointment.getStartTime());
    }
}
