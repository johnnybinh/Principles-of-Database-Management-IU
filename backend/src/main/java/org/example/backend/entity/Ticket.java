package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "Ticket")
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TicketID", nullable = false)
    private Integer ticketID;

    @ManyToOne
    @JoinColumn(name = "PassengerID", nullable = false)
    private Passenger passengerID;

    @ManyToOne
    @JoinColumn(name = "Seat_number", nullable = false)
    private Seat seatNumber;

    @ManyToOne
    @JoinColumn(name = "BookingID", nullable = false)
    private Booking bookingID;

    @ManyToOne
    @JoinColumn(name = "FlightID", nullable = false)
    private FlightBase flightID;

    @Column(name = "Final_price", nullable = false)
    private Integer finalPrice;

    @Column(name = "Baggage_weight", nullable = false)
    private Integer baggageWeight;
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
