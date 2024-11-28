package org.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.backend.entity.Ticket;
import org.example.backend.service.TicketService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {

        private final TicketService ticketService;

        public TicketController(TicketService ticketService) {
            this.ticketService = ticketService;
        }

        @GetMapping("/api/tickets")
        public ResponseEntity<?> getAllTickets() {
            return ResponseEntity.ok(ticketService.getAllTickets());
        }

        @GetMapping("/api/tickets/{id}")
        public ResponseEntity<?> getTicketById(@PathVariable Long id) {
            Ticket ticket = ticketService.getTicketById(id);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ticket);
        }

        @PostMapping("/api/tickets")
        public ResponseEntity<?> saveTicket(@RequestBody Ticket ticket) {
            return ResponseEntity.ok(ticketService.saveTicket(ticket));
        }

        @DeleteMapping("/api/tickets/{id}")
        public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
            ticketService.deleteTicket(id);
            return ResponseEntity.ok().build();
        }

    @GetMapping("/api/tickets/email/{email}")
    public ResponseEntity<?> getTicketsByEmail(@PathVariable String email) {
        List<Ticket> tickets = ticketService.getTicketsByEmail(email);
        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No tickets found for the provided email.");
        }
        return ResponseEntity.ok(tickets);
    }

}
