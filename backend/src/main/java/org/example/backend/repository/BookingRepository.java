package org.example.backend.repository;

import org.example.backend.entity.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT b FROM Booking b ORDER BY b.bookingID DESC LIMIT 1")
    Optional<Booking> findTopByOrderByBookingIDDesc();
}