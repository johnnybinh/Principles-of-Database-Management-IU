package org.example.backend.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "Gate")
@Entity
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Gate_number", nullable = false)
    private Long gateNumber;

    @ManyToOne
    @JoinColumn(name = "AirportID", nullable = false)
    private Airport airportID;

    @Column(name = "Status", nullable = false)
    private String status;

}
