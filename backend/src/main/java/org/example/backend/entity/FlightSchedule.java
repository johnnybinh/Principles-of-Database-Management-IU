package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Flight_Schedule")
@Entity
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleID", nullable = false)
    private Long scheduleID;

    @ManyToOne
    @JoinColumn(name = "StatusID", nullable = false)
    private FlightStatus statusID;

    @Column(name = "Departure_date", nullable = false)
    private String departureDate;

    @Column(name = "Arrival_date", nullable = false)
    private String arrivalDate;
}
