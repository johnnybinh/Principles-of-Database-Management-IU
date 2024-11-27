package org.example.backend.dto;

import org.example.backend.controller.FlightScheduleController;
import org.example.backend.entity.FlightSchedule;
import org.example.backend.service.FlightScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
