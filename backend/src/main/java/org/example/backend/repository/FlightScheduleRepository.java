package org.example.backend.repository;

import org.example.backend.entity.FlightSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long> {
    @Query("SELECT f FROM FlightSchedule f WHERE " +
            "(:departure IS NULL OR f.departure = :departure) AND " +
            "(:arrival IS NULL OR f.arrival = :arrival) AND " +
            "(:departureDate IS NULL OR DATE(f.departureDate) = DATE(:departureDate))")
    List<FlightSchedule> findByFilters(
            @Param("departure") String departure,
            @Param("arrival") String arrival,
            @Param("departureDate") LocalDate departureDate
    );
}
