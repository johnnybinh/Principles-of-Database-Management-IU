package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Seat")
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Seat_number", nullable = false)
    private Integer seatNumber;

    @ManyToOne
    @JoinColumn(name = "Class_type", nullable = false)
    private SeatClass classType;
}

//CREATE TABLE Seat (
//  Seat_number INT PRIMARY KEY,
//  Class_type NVARCHAR(50),
//  CONSTRAINT fk_seat_class FOREIGN KEY (Class_type) REFERENCES Seat_Class(Class_type) ON DELETE CASCADE
//);
