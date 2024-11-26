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
    private Long scheduleID;

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
}

//CREATE TABLE Flight_Schedule (
//  ScheduleID INT PRIMARY KEY,
//  StatusID INT,
//  Departure_date NVARCHAR(50) NOT NULL,
//  Arrival_date NVARCHAR(50) NOT NULL,
//  Departure NVARCHAR(50) NOT NULL,
//  Arrival NVARCHAR(50) NOT NULL,
//  CONSTRAINT fk_status FOREIGN KEY (StatusID) REFERENCES Flight_Status(StatusID) ON DELETE CASCADE
//);