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
    private Integer flightCrewID;

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

//CREATE TABLE Flight_Crew (
//  Flight_Crew_ID INT PRIMARY KEY,
//  EmployeeID INT,
//  FlightID INT,
//  Role NVARCHAR(50),
//  Status NVARCHAR(50),
//  CONSTRAINT fk_employee_crew FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE,
//  CONSTRAINT fk_flight FOREIGN KEY (FlightID) REFERENCES Flight_Base(FlightID) ON DELETE CASCADE
//);
