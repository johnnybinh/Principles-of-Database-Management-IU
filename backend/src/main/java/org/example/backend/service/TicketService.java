package org.example.backend.service;
import org.example.backend.entity.Ticket;
import org.example.backend.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
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
    public void deleteByTicketID(String ticketID) {
        ticketRepository.deleteByTicketID(ticketID);
    }

}

