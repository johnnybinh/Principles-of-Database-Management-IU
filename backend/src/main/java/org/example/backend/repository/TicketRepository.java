package org.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.example.backend.entity.Ticket;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.passenger.email = :email")
    List<Ticket> findAllByPassengerID_Email(String email);

    @Modifying  // Required for modifying queries
    @Transactional // Ensures transaction management
    @Query("DELETE FROM Ticket t WHERE t.ticketID = :ticketID")
    void deleteByTicketID(@Param("ticketID") String ticketID);

}
