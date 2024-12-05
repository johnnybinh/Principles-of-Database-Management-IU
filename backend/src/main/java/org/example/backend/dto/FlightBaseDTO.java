package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightBaseDTO {
    private int gateNumber;
    private AirlineDTO airline;
    private FlightScheduleDTO flightSchedule;
    private AirportDTO departureAirport;

    public FlightBaseDTO(int gateNumber, AirlineDTO airline, FlightScheduleDTO flightSchedule, AirportDTO departureAirport) {
        this.gateNumber = gateNumber;
        this.airline = airline;
        this.flightSchedule = flightSchedule;
        this.departureAirport = departureAirport;
    }

}
