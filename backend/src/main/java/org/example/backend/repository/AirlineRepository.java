package org.example.backend.repository;

import org.example.backend.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    @Query("SELECT a FROM Airline a WHERE a.airlineName = :airlineName")
    List<Airline> findByAirlineName(@Param("airlineName") String airlineName);

    @Query("SELECT a FROM Airline a WHERE a.airlineName = :airlineName")
    Optional<Airline> findFirstByAirlineName(@Param("airlineName") String airlineName);
}
