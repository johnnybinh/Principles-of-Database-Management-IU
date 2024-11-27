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
    private String flightID;

    @ManyToOne
    @JoinColumn(name = "gate_number", nullable = false)
    private Gate gateNumber;

    @ManyToOne
    @JoinColumn(name = "airlineid", nullable = false)
    private Airline airlineID;

    @ManyToOne
    @JoinColumn(name = "scheduleid", nullable = false)
    private FlightSchedule scheduleID;

}
