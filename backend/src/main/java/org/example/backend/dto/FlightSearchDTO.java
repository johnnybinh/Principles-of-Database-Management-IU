package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightSearchDTO {

        private String departure;
        private String arrival;
        private String departureDate;
        private String arrivalDate;

        // Fetch all flight schedules

}
