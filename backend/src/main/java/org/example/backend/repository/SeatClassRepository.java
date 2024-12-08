package org.example.backend.repository;

import org.example.backend.entity.SeatClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {

    @Query("SELECT s FROM SeatClass s WHERE s.classType = :type")
    Optional<SeatClass> findByType(String type);

}
