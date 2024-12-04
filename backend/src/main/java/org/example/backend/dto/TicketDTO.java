package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketDTO {

    private PassengerDTO passenger;
    private BookingDTO booking;
    private SeatDTO seat;
    private String flightScheduleID;
    private double finalPrice;
    private double baggageWeight;
}
