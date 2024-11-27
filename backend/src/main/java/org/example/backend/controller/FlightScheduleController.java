package org.example.backend.controller;

import org.example.backend.dto.FlightSearchDTO;
import org.example.backend.entity.FlightSchedule;
import org.example.backend.service.FlightScheduleService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flight_schedules")
@CrossOrigin(origins = "http://localhost:3000") // Adjust based on your front-end's origin
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    // Fetch all flight schedules
//    @GetMapping
//    public ResponseEntity<List<FlightSchedule>> getAllSchedules(@RequestBody(required = false) FlightSearchDTO request) {
//        if (request == null) {
//            request = new FlightSearchDTO(); // Initialize with default values if request body is missing
//        }
//        List<FlightSchedule> schedules = flightScheduleService.getAllSchedules(
//                request.getDeparture(),
//                request.getArrival(),
//                request.getDepartureDate(),
//                request.getArrivalDate()
//        );
//        return ResponseEntity.ok(schedules);
//    }

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