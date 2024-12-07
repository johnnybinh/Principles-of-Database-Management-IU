package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.*;

import org.example.backend.repository.*;
import org.example.backend.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightBaseRepository flightBaseRepository;

    @Autowired
    private IDGenerator idGenerator = new IDGenerator();

    // Add constants for ID prefixes and digits
    private static final String PASSENGER_PREFIX = "PA";
    private static final String BOOKING_PREFIX = "BK";
    private static final String TICKET_PREFIX = "TK";
    private static final int ID_DIGITS = 3;

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

    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByEmail(String email) {
        return ticketRepository.findAllByPassengerID_Email(email);
    }

    public String getAirlineIdByName(String airlineName) {
        return airlineRepository.findFirstByAirlineName(airlineName)
            .orElseThrow(() -> new RuntimeException("Airline not found with name: " + airlineName))
            .getAirlineID();
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
        // Generate and set passengerID
        passenger.setPassengerID(generatePassengerID());
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
        
        // Handle null BookingDTO
        if (bookingDTO == null) {
            booking.setBookingDate(Date.valueOf(LocalDateTime.now().toLocalDate()));
            return booking;
        }
        
        // Safe conversion with null checks
        booking.setBookingDate(bookingDTO.getBookingDate() != null ? 
            bookingDTO.getBookingDate() : 
            Date.valueOf(LocalDateTime.now().toLocalDate()));
        
        return booking;
    }

    private Seat convertToEntity(SeatDTO seatDTO) {
        Seat seat = new Seat();
        seat.setSeatClass(seatClassRepository.findByType(seatDTO.getSeatClass().getClassType()).orElse(null));
        return seat;
    }

    private FlightBase convertToEntity(FlightBaseDTO flightBaseDTO) {
        FlightBase flightBase = new FlightBase();
        
        // Handle null FlightBaseDTO
        if (flightBaseDTO == null) {
            return flightBase;
        }
        
        // Handle Airline conversion with null checks
        if (flightBaseDTO.getAirline() != null && flightBaseDTO.getAirline().getAirlineName() != null) {
            Airline airline = new Airline("Unknown");
            airline.setAirlineName(flightBaseDTO.getAirline().getAirlineName());
            flightBase.setAirline(airline);
        }
        
        // Handle FlightSchedule conversion with null checks
        if (flightBaseDTO.getFlightSchedule() != null) {
            FlightSchedule schedule = new FlightSchedule();
            schedule.setScheduleID(flightBaseDTO.getFlightSchedule().getScheduleID());
            flightBase.setFlightSchedule(schedule);
        }
        
        return flightBase;
    }

    // ===========================================================================
    // Generate ID
    private String generatePassengerID() {
        String lastId = passengerRepository.findFirstByOrderByPassengerIDDesc()
                .map(Passenger::getPassengerID)
                .orElse("PA000"); // Start from PA001 if no passengers exist
        return idGenerator.generateID(lastId, PASSENGER_PREFIX, ID_DIGITS);
    }

    private String generateBookingID() {
        String lastId = bookingRepository.findFirstByOrderByBookingIDDesc()
                .map(Booking::getBookingID)
                .orElse("BK000"); // Start from PA001 if no passengers exist
        return idGenerator.generateID(lastId, BOOKING_PREFIX, ID_DIGITS);
    }

    private String generateTicketID() {
        String lastId = ticketRepository.findFirstByOrderByTicketIDDesc()
                .map(Ticket::getTicketID)
                .orElse("TK000"); // Start from TK001 if no tickets exist
        return idGenerator.generateID(lastId, TICKET_PREFIX, ID_DIGITS);
    }

    // ===========================================================================
    // Create Ticket
    @Transactional
    public Ticket createTicket(TicketDTO ticketDTO) {
        if (ticketDTO.getFlight() == null || 
            ticketDTO.getFlight().getAirline() == null || 
            ticketDTO.getFlight().getAirline().getAirlineName() == null) {
            throw new IllegalArgumentException("Flight and airline information are required");
        }

        // Convert and save passenger with generated ID
        Passenger passenger = ticketDTO.getPassenger() != null ?
                convertToEntity(ticketDTO.getPassenger()) :
                new Passenger();
        String passengerId = generatePassengerID();
        passenger.setPassengerID(passengerId);
        passenger = passengerRepository.save(passenger);

        // Create and save booking with generated ID
        Booking booking = convertToEntity(ticketDTO.getBooking());
        String bookingId = generateBookingID();
        booking.setBookingID(bookingId);
        booking.setBookingDate(Date.valueOf(LocalDateTime.now().toLocalDate()));
        booking = bookingRepository.save(booking);

        // Flight Base

        // Create and save the ticket with generated ID
        String ticketId = generateTicketID();

        // Create and save ticket with all references
        Ticket ticket = new Ticket();
        ticket.setTicketID(ticketId);
        ticket.setPassenger(passenger);
        ticket.setBooking(booking);
        ticket.setFinalPrice(ticketDTO.getFinalPrice());
        ticket.setBaggageWeight(ticketDTO.getBaggageWeight());

        ticket = ticketRepository.save(ticket);

        return ticket;
    }

    // Add helper conversion methods
    private PassengerDTO convertToDTO(Passenger passenger) {
        return new PassengerDTO(
            passenger.getFirstName(),
            passenger.getLastName(),
            passenger.getAge(),
            passenger.getEmail(),
            passenger.getPhoneNumber(),
            passenger.getPassportNumber(),
            passenger.getDateOfBirth(),
            passenger.getNationality(),
            passenger.getAddress()
        );
    }

    private BookingDTO convertToDTO(Booking booking) {
        booking.setPaymentStatus("Paid"); // Set default payment status
        return new BookingDTO(
            booking.getBookingDate(),
            booking.getPaymentStatus()
        );
    }

    // Add SeatClass to DTO conversion
    private SeatClassDTO convertToDTO(SeatClass seatClass) {
        return new SeatClassDTO(
            seatClass.getClassType()
        );
    }

    // Update SeatDTO conversion
    private SeatDTO convertToDTO(Seat seat) {
        return new SeatDTO(
            convertToDTO(seat.getSeatClass()) // Convert SeatClass to SeatClassDTO
        );
    }

    // Add Airline to DTO conversion
    private AirlineDTO convertToDTO(Airline airline) {
        return new AirlineDTO(
            airline.getAirlineName()
        );
    }

    // Add FlightStatus to DTO conversion
    private FlightStatusDTO convertToDTO(FlightStatus flightStatus) {
        return new FlightStatusDTO(
            flightStatus.getStatus()
        );
    }

    // Update FlightSchedule to DTO conversion
    private FlightScheduleDTO convertToDTO(FlightSchedule flightSchedule) {
        return new FlightScheduleDTO(
            convertToDTO(flightSchedule.getFlightStatus()),  // Convert FlightStatus to DTO
            flightSchedule.getDepartureDate(),
            flightSchedule.getArrivalDate(),
            flightSchedule.getDeparture(),
            flightSchedule.getArrival(),
            flightSchedule.getFlightDuration()
        );
    }

    // Add Airport to DTO conversion
    private AirportDTO convertToDTO(Airport airport) {
        return new AirportDTO(
            airport.getAirportName(),
            airport.getCity(),
            airport.getCountry()
        );
    }

    // Update FlightBase to DTO conversion
    private FlightBaseDTO convertToDTO(FlightBase flightBase) {
        Random random = new Random();
        flightBase.setGateNumber(random.nextInt(20) + 1);
        return new FlightBaseDTO(
            flightBase.getGateNumber(),
            convertToDTO(flightBase.getAirline()),
            convertToDTO(flightBase.getFlightSchedule()),
            convertToDTO(flightBase.getAirport()) // Add airport conversion
        );
    }
}

