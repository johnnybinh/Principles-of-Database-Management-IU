package org.example.backend.service;

import org.example.backend.entity.FlightSchedule;
import org.example.backend.repository.FlightScheduleRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;

    public FlightScheduleService(FlightScheduleRepository flightScheduleRepository) {
        this.flightScheduleRepository = flightScheduleRepository;
    }

    public List<FlightSchedule> getAllSchedules(String departure, String arrival, String departureDate, String arrivalDate) {
        return flightScheduleRepository.findAll();
    }

    // In FlightScheduleService.java, update the getAllSchedules method:
    public List<FlightSchedule> getAllSchedulesByFilter(String departure, String arrival, String departureDate, String arrivalDate) {
        if (departure == null && arrival == null && departureDate == null && arrivalDate == null) {
            return flightScheduleRepository.findAll();
        }

        LocalDate parsedDepartureDate = null;
        try {
            if (departureDate != null && !departureDate.isEmpty()) {
                parsedDepartureDate = LocalDate.parse(departureDate);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD", e);
        }

        return flightScheduleRepository.findByFilters(departure, arrival, parsedDepartureDate);
    }
}

