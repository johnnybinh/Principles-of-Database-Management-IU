package org.example.backend.repository;

import org.example.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

//    @Query("SELECT s FROM Seat s " +
//            "JOIN s.flightSchedule fs ON fs.scheduleID = :scheduleId " +
//            "WHERE s.seatClass.classType = :seatClassType " +
//            "ORDER BY s.seatNumber ASC")
//    List<Seat> findAvailableSeatsByScheduleAndClass(
//            @Param("scheduleId") String scheduleId,
//            @Param("seatClassType") String seatClassType
//    );
//
//    @Modifying
//    @Query("UPDATE Seat s SET s.status = 'BOOKED' WHERE s.seatID = :seatId")
//    void updateSeatStatus(@Param("seatId") String seatId);
}
