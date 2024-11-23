package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Employee")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID", nullable = false)
    private Long employeeID;

    @Column(name = "First_Name", nullable = false)
    private String firstName;

    @Column(name = "Last_Name", nullable = false)
    private String lastName;

    @Column(name = "Gender", nullable = false)
    private char gender;

    @Column(name = "Date_of_Birth", nullable = false)
    private String dateOfBirth;
    // Check if dateOfBirth is in the past
    // Check if dateOfBirth is in the format dd-mm-yyyy

    @Column(name = "Age", nullable = false)
    private int age; // Check if age >= 18 and <= 100

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Salary", nullable = false)
    private double salary; // Check if salary >= 0

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Phone_Number", nullable = false)
    private String phoneNumber; // Check if phoneNumber is in the format 0xxxxxxxxx

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
