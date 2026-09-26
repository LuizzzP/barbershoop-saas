package com.barbershop.entities;

import java.time.LocalTime;

public class WorkShift {
    private LocalTime openingTime;
    private LocalTime closingTime;

    public WorkShift(LocalTime openingTime, LocalTime closingTime) {
        if (closingTime.isBefore(openingTime) || closingTime.equals(openingTime)) {
            throw new IllegalArgumentException("O horário de fechamento deve ser posterior ao de abertura.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}