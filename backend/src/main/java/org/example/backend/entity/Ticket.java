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
    @Column(name = "ticketid", nullable = false, length=5)
    private String ticketID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passengerid", nullable = false)
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_number", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingID", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flightID", nullable = false)
    private FlightBase flightBase;

    @Column(name = "final_price", nullable = false)
    private double finalPrice;

    @Column(name = "baggage_weight", nullable = false)
    private double baggageWeight;
}
