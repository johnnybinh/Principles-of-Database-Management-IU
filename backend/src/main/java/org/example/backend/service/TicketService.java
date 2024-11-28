package org.example.backend.service;
import org.example.backend.entity.Ticket;
import org.example.backend.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service


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
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByEmail(String email) {
        return ticketRepository.findAllByPassengerID_Email(email);
    }

}

