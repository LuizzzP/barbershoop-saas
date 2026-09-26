package com.barbershop.entities;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exceptions.AppointmentConflictException;
import com.barbershop.exceptions.ShopClosedException;

public class Shop {
	private long id;
	private String name;
	private List<Appointment> appointments = new ArrayList<>();
	private List<ServiceProvided> services = new ArrayList<>();
	private List<WorkShift> workShifts = new ArrayList<>();
	private EnumSet <DayOfWeek> closedDays = EnumSet.noneOf(DayOfWeek.class);
	
	
	public Shop() {}
	
	public Shop(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	
	public void addClosedDay(DayOfWeek e) {
		closedDays.add(e);
	}
	
	public boolean isShopClosedInDay(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return closedDays.contains(dayOfWeek);
	}
	
	public boolean isAppointmentExists(LocalDateTime startTime, LocalDateTime endTime) {
	    return appointments.stream()
	        .filter(appointment -> appointment.getStatus() != AppointmentStatus.AVAILABLE)
	        .anyMatch(appointment ->
	            startTime.isBefore(appointment.getEndTime()) &&
	            appointment.getStartTime().isBefore(endTime)
	        );
	}
	
	public void makeAppointment(Appointment appointment) {
	    LocalDate date = appointment.getStartTime().toLocalDate();

	    if (isShopClosedInDay(date)) {
	        throw new ShopClosedException(this, date);
	    }

	    if (appointment.getStatus() == AppointmentStatus.AVAILABLE) {
	        throw new IllegalArgumentException("Um agendamento marcado não pode ter status AVAILABLE.");
	    }

	    if (isAppointmentExists(appointment.getStartTime(), appointment.getEndTime())) {
	        throw new AppointmentConflictException(this, appointment);
	    }
	    

	    appointments.add(appointment);
	}

	public List<Appointment> getAvailableAppointments(LocalDate date, ServiceProvided service) {
		if(isShopClosedInDay(date)) throw new ShopClosedException(this, date);
	    List<Appointment> appointmentList = new ArrayList<>();

	    for (int i = 0; i < workShifts.size(); i++) {
	        LocalTime openingTime = workShifts.get(i).getOpeningTime();
	        LocalTime closingTime = workShifts.get(i).getClosingTime();
	        long shiftMinutes = Duration.between(openingTime, closingTime).toMinutes();
	        int totalApointments = Math.toIntExact(shiftMinutes / 30);

	        for (int j = 0; j < totalApointments; j++) {
	            LocalTime slotTime = openingTime.plusMinutes(j * 30L);
	            LocalDateTime timeDate = date.atTime(slotTime);
	            LocalDateTime timeDateEnd = timeDate.plus(service.getMinutesDuration());

	            if (timeDateEnd.toLocalTime().isAfter(closingTime) || !timeDateEnd.toLocalDate().equals(date)) {
	                continue;
	            }

	            if (!isAppointmentExists(timeDate, timeDateEnd)) {
	                Appointment appointment = new Appointment(
	                    AppointmentStatus.AVAILABLE,
	                    timeDate,
	                    timeDateEnd,
	                    null,
	                    null,
	                    null
	                );
	                appointmentList.add(appointment);
	            }
	        }
	    }
	    return appointmentList;
	}
	
	
	public List<Appointment> getAppointmentList() {
		return appointments;
	}
	
	public List<WorkShift> getWorkShiftList() {
		return workShifts;
	}
	
	public void addServiceProvided(ServiceProvided service) {
		services.add(service);
	}
	
	public void addWorkShift(WorkShift workShift) {
		workShifts.add(workShift);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getId( ) {
		return id;
	}
	
	public List<ServiceProvided> getServiceList() {
		return services;
	}
}
