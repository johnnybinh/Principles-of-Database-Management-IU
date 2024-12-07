package org.example.backend.repository;

import org.example.backend.entity.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository <Booking, Long> {

    @Query("SELECT b FROM Booking b ORDER BY b.bookingID DESC LIMIT 1")
    Optional<Booking> findFirstByOrderByBookingIDDesc();
}
