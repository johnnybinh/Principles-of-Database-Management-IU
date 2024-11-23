package org.example.backend.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "Airport_staff")
@Entity
public class AirportStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Airport_staff_ID", nullable = false)
    private Long airportStaffID;

    @ManyToOne
    @JoinColumn(name = "AirportID", nullable = false)
    private Airport airportID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Status", nullable = false)
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
