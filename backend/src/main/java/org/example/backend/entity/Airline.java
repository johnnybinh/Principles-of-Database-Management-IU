package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "airline")
@Entity
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AirlineID", nullable = false)
    private Long airlineID;

    @Column(name = "Airline_name", nullable = false)
    private String airlineName;

}

//CREATE TABLE Airline (
//  AirlineID INT PRIMARY KEY,
//  Airline_name NVARCHAR(100)
//);