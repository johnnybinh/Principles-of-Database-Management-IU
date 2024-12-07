package org.example.backend.controller;

import org.example.backend.entity.FlightSchedule;
import org.example.backend.service.FlightScheduleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight_schedules")
@CrossOrigin(origins = "http://localhost:3000") // Adjust based on your front-end's origin
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    // Fetch schedules based on input (filters)
    // In FlightScheduleController.java, update the getAllSchedules method:
    @GetMapping
    public ResponseEntity<List<FlightSchedule>> getAllSchedulesByFilter(
            @RequestParam(required = false) String departure,
            @RequestParam(required = false) String arrival,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) String arrivalDate
    ) {
        List<FlightSchedule> schedules = flightScheduleService.getAllSchedulesByFilter(
                departure,
                arrival,
                departureDate,
                arrivalDate
        );
        return ResponseEntity.ok(schedules);
    }

}