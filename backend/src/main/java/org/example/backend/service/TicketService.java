package org.example.backend.service;
import org.example.backend.entity.Ticket;
import org.example.backend.repository.TicketRepository;
import org.example.backend.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;

    public TicketService(TicketRepository ticketRepository, PassengerRepository passengerRepository) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
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

}

