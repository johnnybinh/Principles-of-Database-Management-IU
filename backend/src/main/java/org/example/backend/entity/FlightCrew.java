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
    @Column(name = "Flight_Crew_ID", nullable = false)
    private Integer flightCrewID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @ManyToOne
    @JoinColumn(name = "FlightID", nullable = false)
    private FlightBase flightID;

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Status", nullable = false)
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
