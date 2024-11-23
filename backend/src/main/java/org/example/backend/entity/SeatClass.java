package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Seat_Class")
@Entity
public class SeatClass {

    @Id
    @Column(name = "Class_type", nullable = false)
    private String classType;

    @Column(name = "Base_price", nullable = false)
    private float basePrice;
}

// CREATE TABLE Seat_Class (
//  Class_type NVARCHAR(50) PRIMARY KEY,
//  Base_price DECIMAL(18, 2)
//);
