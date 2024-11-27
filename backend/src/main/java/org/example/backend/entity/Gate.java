package org.example.backend.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Table(name = "gate")
@Entity
public class Gate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gate_number", nullable = false)
    private String gateNumber;

    @ManyToOne
    @JoinColumn(name = "airportid", nullable = false)
    private Airport airportID;

    @Column(name = "status", nullable = false)
    private String status;

}
