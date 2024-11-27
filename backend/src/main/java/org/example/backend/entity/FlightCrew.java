package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "flight_crew")
@Entity
public class FlightCrew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_crew_id", nullable = false)
    private String flightCrewID;

    @ManyToOne
    @JoinColumn(name = "employeeid", nullable = false)
    private Employee employeeID;

    @ManyToOne
    @JoinColumn(name = "flightid", nullable = false)
    private FlightBase flightID;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "status", nullable = false)
    private String status;
}
