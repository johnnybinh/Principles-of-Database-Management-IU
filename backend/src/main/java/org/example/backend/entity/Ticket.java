package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "ticket")
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticketid", nullable = false)
    private String ticketID;

    @ManyToOne
    @JoinColumn(name = "passengerid", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "seat_number", nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "flightID", nullable = false)
    private FlightBase flightBase;

    @Column(name = "final_price", nullable = false)
    private double finalPrice;

    @Column(name = "baggage_weight", nullable = false)
    private double baggageWeight;
}

//CREATE TABLE Ticket (
//  TicketID INT PRIMARY KEY,
//  PassengerID INT,
//  Seat_number INT,
//  BookingID INT,
//  FlightID INT,
//  Final_price INT CHECK (Final_price >= 0),
//  Baggage_weight INT CHECK (Baggage_weight >= 0),
//  CONSTRAINT fk_passenger FOREIGN KEY (PassengerID) REFERENCES Passenger(PassengerID) ON DELETE CASCADE,
//  CONSTRAINT fk_seat FOREIGN KEY (Seat_number) REFERENCES Seat(Seat_number) ON DELETE CASCADE,
//  CONSTRAINT fk_booking FOREIGN KEY (BookingID) REFERENCES Booking(BookingId) ON DELETE CASCADE,
//  CONSTRAINT fk_flight_ticket FOREIGN KEY (FlightID) REFERENCES Flight_Base(FlightID) ON DELETE CASCADE
//);
