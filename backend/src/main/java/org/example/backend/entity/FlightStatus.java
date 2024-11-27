package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "flight_status")
@Entity
public class FlightStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusid", nullable = false)
    private String statusID;

    @Column(name = "status", nullable = false)
    private String status;
}
