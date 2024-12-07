package org.example.backend.service;

import org.example.backend.dto.*;
import org.example.backend.entity.*;

import org.example.backend.repository.*;
import org.example.backend.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.ConcurrentModificationException;

@Service
@Slf4j
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

    private final Object idLock = new Object();

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

    public Ticket getTicketById(String id) {
        return ticketRepository.findByTicketID(id);
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
        
        booking.setPaymentStatus("Pending");
        booking.setBookingDate(bookingDTO != null && bookingDTO.getBookingDate() != null ? 
            bookingDTO.getBookingDate() : 
            Date.valueOf(LocalDateTime.now().toLocalDate()));
        
        if (bookingDTO != null && bookingDTO.getPaymentStatus() != null) {
            booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        }
        
        return booking;  // Remove ID generation here
    }
    
    private void validateBooking(Booking booking) {
        if (booking.getBookingID() == null || 
            booking.getBookingDate() == null || 
            booking.getPaymentStatus() == null) {
            throw new IllegalStateException("Booking missing required fields");
        }
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

    @Transactional(isolation = Isolation.SERIALIZABLE)
    protected String generateBookingID() {
        String lastId = bookingRepository.findTopByOrderByBookingIDDesc()
            .map(Booking::getBookingID)
            .orElse("BK000");
        
        log.debug("Last booking ID found: {}", lastId);
        String newId = idGenerator.generateID(lastId, "BK", 3);
        log.debug("Generated new booking ID: {}", newId);
        
        return newId;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        try {
            return createTicketInternal(ticketDTO);
        } catch (Exception e) {
            log.error("Error creating ticket: ", e);
            throw new RuntimeException("Failed to create ticket", e);
        }

    }

    private TicketDTO createTicketInternal(TicketDTO ticketDTO) {
        // Generate booking ID with synchronization
        String bookingId;
        synchronized (idLock) {
            bookingId = generateBookingID();
            // Verify booking doesn't exist
            if (bookingRepository.findById(bookingId).isPresent()) {
                throw new ConcurrentModificationException("Booking ID already exists: " + bookingId);
            }
        }
        log.info("Creating new booking with ID: {}", bookingId);

        // Validate flight info
        if (ticketDTO.getFlight() == null || 
            ticketDTO.getFlight().getAirline() == null || 
            ticketDTO.getFlight().getAirline().getAirlineName() == null) {
            log.warn("Invalid flight or airline information");
            throw new IllegalArgumentException("Flight and airline information are required");
        }

        // Handle passenger with existing passport number
        Passenger passenger;
        if (ticketDTO.getPassenger() != null && ticketDTO.getPassenger().getPassportNumber() != null) {
            passenger = passengerRepository.findByPassportNumber(ticketDTO.getPassenger().getPassportNumber())
                    .orElseGet(() -> {
                        Passenger newPassenger = convertToEntity(ticketDTO.getPassenger());
                        return passengerRepository.save(newPassenger);
                    });
        } else {
            passenger = new Passenger();
            passenger = passengerRepository.save(passenger);
        }
        log.debug("Using passenger with ID: {}", passenger.getPassengerID());

        // Create and save booking with synchronization
        Booking booking = convertToEntity(ticketDTO.getBooking());
        booking.setBookingID(bookingId);
        booking.setBookingDate(Date.valueOf(LocalDate.now()));
        
        synchronized (idLock) {
            if (bookingRepository.findById(bookingId).isPresent()) {
                throw new ConcurrentModificationException("Booking ID collision detected: " + bookingId);
            }
            booking = bookingRepository.save(booking);
        }
        log.debug("Booking saved with ID: {}", booking.getBookingID());
        
        // Create and save seat
        Seat seat = null;
        if (ticketDTO.getSeat() != null) {
            seat = convertToEntity(ticketDTO.getSeat());
            seat = seatRepository.save(seat);
        }
        log.debug("Seat created and saved: {}", seat);
        
        
        // Create and save ticket
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setPassenger(passenger);
        ticket.setSeat(seat);
        ticket.setFinalPrice(ticketDTO.getFinalPrice());
        ticket.setBaggageWeight(ticketDTO.getBaggageWeight());
        
        // Save the complete ticket
        ticket = ticketRepository.save(ticket);
        log.debug("Ticket saved with dependencies");

        return convertToDTO(ticket);
    }

    //============================================================================
    // Conversion methods
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

