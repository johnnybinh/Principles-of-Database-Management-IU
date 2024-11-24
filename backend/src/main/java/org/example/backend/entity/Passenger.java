package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Table(name = "passenger")
@Entity
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PassengerID", nullable = false)
    private Integer passengerID;

    @Column(name = "First_name", nullable = false)
    private String firstName;

    @Column(name = "Last_name", nullable = false)
    private String lastName;

    @Column(name = "Age", nullable = false)
    private Integer age;

    @Column(name = "Date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "Nationality", nullable = false)
    private String nationality;

    @Column(name = "Passport_number", nullable = false, unique = true)
    private String passportNumber;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "Address", nullable = false)
    private String address;
}

//CREATE TABLE Passenger (
//  PassengerID INT PRIMARY KEY,
//  First_name NVARCHAR(50),
//  Last_name NVARCHAR(50),
//  Age INT CHECK (Age >= 0 AND Age <= 120),
//  Date_of_birth DATE,
//  Nationality NVARCHAR(50),
//  Passport_number NCHAR(9) UNIQUE,
//  Email NVARCHAR(100),
//  Phone_number NCHAR(12),
//  Address NVARCHAR(200)
//);
