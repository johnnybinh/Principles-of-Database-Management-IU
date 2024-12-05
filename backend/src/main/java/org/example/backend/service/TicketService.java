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
    private IDGenerator idGenerator = new IDGenerator();

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightBaseRepository flightBaseRepository;

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

    private String generatePassengerID() {
        String lastId = passengerRepository.findTopByOrderByPassengerIDDesc()
                .map(Passenger::getPassengerID)
                .orElse("PA000"); // Start from PA001 if no passengers exist
        return idGenerator.generateID(lastId, "PA", 3);
    }

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        if (ticketDTO.getFlight() == null || 
            ticketDTO.getFlight().getAirline() == null || 
            ticketDTO.getFlight().getAirline().getAirlineName() == null) {
            throw new IllegalArgumentException("Flight and airline information are required");
        }

        // Convert and save passenger first
        Passenger passenger = ticketDTO.getPassenger() != null ?
                convertToEntity(ticketDTO.getPassenger()) :
                new Passenger();
        passenger = passengerRepository.save(passenger);

        // Create and save booking with passenger reference
        Booking booking = convertToEntity(ticketDTO.getBooking());
        booking.setBookingDate(Date.valueOf(LocalDateTime.now().toLocalDate()));
        booking = bookingRepository.save(booking);

        // Handle seat
        Seat seat = ticketDTO.getSeat() != null ?
                convertToEntity(ticketDTO.getSeat()) :
                new Seat();
        seat = seatRepository.save(seat);

        // Handle flight base
        FlightBase flightBase = ticketDTO.getFlight() != null ?
                convertToEntity(ticketDTO.getFlight()) :
                new FlightBase();
        flightBase = flightBaseRepository.save(flightBase);

        // Fetch and set IDs
        String airlineID = getAirlineIdByName(flightBase.getAirline().getAirlineName());
        Seat finalSeat = seat;
        SeatClass seatClass = seatClassRepository.findByType(seat.getSeatClass().getClassType())
                .orElseThrow(() -> new RuntimeException("Seat class not found with type: " + finalSeat.getSeatClass().getClassType()));

        flightBase.getAirline().setAirlineID(airlineID);
        seat.setSeatClass(seatClass);

        // Create and save ticket with all references
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setBooking(booking);
        ticket.setSeat(seat);
        ticket.setFlightBase(flightBase);
        ticket.setFinalPrice(ticketDTO.getFinalPrice());
        ticket.setBaggageWeight(ticketDTO.getBaggageWeight());

        ticket = ticketRepository.save(ticket);

        return convertToDTO(ticket);
    }

    // Add convertToDTO method
    private TicketDTO convertToDTO(Ticket ticket) {
        return new TicketDTO(
            convertToDTO(ticket.getPassenger()),
            convertToDTO(ticket.getBooking()),
            convertToDTO(ticket.getSeat()),
            convertToDTO(ticket.getFlightBase()),
            ticket.getFinalPrice(),
            ticket.getBaggageWeight()
        );
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

