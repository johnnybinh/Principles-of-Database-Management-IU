package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "airport")
@Entity
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AirportID", nullable = false)
    private Long airportID;

    @Column(name = "Airport_Name", nullable = false)
    private String airportName;

    @Column(name = "City", nullable = false)
    private String city;

    @Column(name = "Country", nullable = false)
    private String country;

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
