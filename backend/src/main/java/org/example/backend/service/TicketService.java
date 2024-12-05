package org.example.backend.service;

import org.example.backend.dto.PassengerDTO;
import org.example.backend.dto.TicketDTO;
import org.example.backend.dto.BookingDTO;
import org.example.backend.dto.SeatDTO;
import org.example.backend.dto.FlightBaseDTO;
import org.example.backend.entity.*;

import org.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {

    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final PassengerRepository passengerRepository;

    @Autowired
    private final AirlineRepository airlineRepository;

    @Autowired
    private final SeatClassRepository seatClassRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    public TicketService(TicketRepository ticketRepository,
                         PassengerRepository passengerRepository,
                         AirlineRepository airlineRepository,
                         SeatClassRepository seatClassRepository,
                         BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.airlineRepository = airlineRepository;
        this.seatClassRepository = seatClassRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByEmail(String email) {
        return ticketRepository.findAllByPassengerID_Email(email);
    }

    public String getAirlineIdByName(String airlineName) {
        return airlineRepository.findFirstByAirlineName(airlineName)
            .orElseThrow(() -> new RuntimeException("Airline not found with name: " + airlineName))
            .getAirlineID();
    }

    public String getSeatClassIdByName(String classType) {
        return seatClassRepository.findByType(classType)
            .orElseThrow(() -> new RuntimeException("Seat class not found with type: " + classType))
            .getClassType();
    }

    @Transactional
    public void deleteTicketAndPassenger(String ticketID) {
        Ticket ticket = ticketRepository.findByTicketID(ticketID);
        if (ticket != null) {
            String passengerID = ticket.getPassenger().getPassengerID();
            ticketRepository.deleteByTicketID(ticketID);
            if (ticketRepository.countByPassengerID(passengerID) == 0) {
                passengerRepository.deleteByPassengerID(passengerID);
            }
        }
    }

    //============================================================================
    // Prepare for creating a new ticket
    //============================================================================
    private Passenger convertToEntity(PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(passengerDTO.getFirstName());
        passenger.setLastName(passengerDTO.getLastName());
        passenger.setAge(passengerDTO.getAge());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setPhoneNumber(passengerDTO.getPhoneNumber());
        passenger.setPassportNumber(passengerDTO.getPassportNumber());
        passenger.setDateOfBirth(passengerDTO.getDateOfBirth());
        passenger.setNationality(passengerDTO.getNationality());
        passenger.setAddress(passengerDTO.getAddress());
        return passenger;
    }

    private Booking convertToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        return booking;
    }

    private Seat convertToEntity(SeatDTO seatDTO) {
        Seat seat = new Seat();
        seat.setSeatClass(seatClassRepository.findByType(seatDTO.getSeatClass().getClassType()).orElse(null));
        return seat;
    }

    private FlightBase convertToEntity(FlightBaseDTO flightBaseDTO) {
        FlightBase flightBase = new FlightBase();
        flightBase.setAirline(new Airline(flightBaseDTO.getAirline().getAirlineName()));
        flightBase.setFlightSchedule(new FlightSchedule(flightBaseDTO.getFlightSchedule().getScheduleID()));
        return flightBase;
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        // Convert DTOs to entities
        Passenger passenger = convertToEntity(ticketDTO.getPassenger());
        Booking booking = convertToEntity(ticketDTO.getBooking());
        Seat seat = convertToEntity(ticketDTO.getSeat());
        FlightBase flightBase = convertToEntity(ticketDTO.getFlight());

        // Fetch IDs
        String airlineID = getAirlineIdByName(flightBase.getAirline().getAirlineName());
        SeatClass seatClass = seatClassRepository.findByType(seat.getSeatClass().getClassType())
                .orElseThrow(() -> new RuntimeException("Seat class not found with type: " + seat.getSeatClass().getClassType()));

        // Set IDs
        flightBase.getAirline().setAirlineID(airlineID);
        seat.setSeatClass(seatClass);

        // Save entities
        passengerRepository.save(passenger);
        bookingRepository.save(booking);
        // Assuming flightBase and seat are saved elsewhere

        // Create and save the ticket
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setBooking(booking);
        ticket.setSeat(seat);
        ticket.setFlightBase(flightBase);
        ticket.setFinalPrice(ticketDTO.getFinalPrice());
        ticket.setBaggageWeight(ticketDTO.getBaggageWeight());
        ticketRepository.save(ticket);

        return ticketDTO;
    }
}

