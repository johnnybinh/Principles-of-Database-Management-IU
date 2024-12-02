package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingTicketDTO {

    private PassengerDTO passenger;
    private String flightScheduleID;
    private String seatClassType;
    private double finalPrice;
    private double baggageWeight;
}
