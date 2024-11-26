package org.example.backend.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "airport_staff")
@Entity
public class AirportStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_staff_id", nullable = false)
    private Long airportStaffID;

    @ManyToOne
    @JoinColumn(name = "airportid", nullable = false)
    private Airport airportID;

    @ManyToOne
    @JoinColumn(name = "employeeid", nullable = false)
    private Employee employeeID;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "status", nullable = false)
    private String status;

}

//CREATE TABLE Airport_staff (
//  Airport_staff_ID INT PRIMARY KEY,
//  AirportID INT,
//  EmployeeID INT,
//  Status NVARCHAR(50) NOT NULL,
//  Role NVARCHAR(50) NOT NULL,
//  CONSTRAINT fk_airport FOREIGN KEY (AirportID) REFERENCES Airport(AirportID) ON DELETE CASCADE,
//  CONSTRAINT fk_employee FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE
//);
