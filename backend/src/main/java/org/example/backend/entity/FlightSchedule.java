package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "flight_schedule")
@Entity
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleid", nullable = false)
    private String scheduleID;

    @ManyToOne
    @JoinColumn(name = "statusid", nullable = false)
    private FlightStatus statusID;

    @Column(name = "departure_date", nullable = false)
    private String departureDate;

    @Column(name = "arrival_date", nullable = false)
    private String arrivalDate;

    @Column(name = "departure", nullable = false)
    private String departure;

    @Column(name = "arrival", nullable = false)
    private String arrival;

    @Column(name = "flight_duration", nullable = false)
    private Float flightDuration;
}
