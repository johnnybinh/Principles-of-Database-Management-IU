package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "flight_base")
@Entity
public class FlightBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flightid", nullable = false)
    private Long flightID;

    @ManyToOne
    @JoinColumn(name = "gate_number", nullable = false)
    private Gate gateNumber;

    @ManyToOne
    @JoinColumn(name = "airlineid", nullable = false)
    private Airline airlineID;

    @ManyToOne
    @JoinColumn(name = "scheduleid", nullable = false)
    private FlightSchedule scheduleID;

    @Column(name = "flight_duration", nullable = false)
    private Integer flightDuration;
}

//CREATE TABLE Flight_Base (
//  FlightID INT PRIMARY KEY,
//  Gate_number INT,
//  AirlineID INT,
//  ScheduleID INT,
//  Flight_duration INT,
//  CONSTRAINT fk_gate FOREIGN KEY (Gate_number) REFERENCES Gate(Gate_number) ON DELETE CASCADE,
//  CONSTRAINT fk_airline FOREIGN KEY (AirlineID) REFERENCES Airline(AirlineID) ON DELETE CASCADE,
//  CONSTRAINT fk_schedule FOREIGN KEY (ScheduleID) REFERENCES Flight_Schedule(ScheduleID) ON DELETE CASCADE
//);
