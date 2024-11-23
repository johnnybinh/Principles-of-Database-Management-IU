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
