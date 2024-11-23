package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Flight_Status")
@Entity
public class FlightStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusID", nullable = false)
    private Long statusID;

    @Column(name = "Status", nullable = false)
    private String status;
}

//CREATE TABLE Flight_Status (
//  StatusID INT PRIMARY KEY,
//  Status NVARCHAR(50)
//);
