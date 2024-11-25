package org.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.backend.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {}
