package org.example.backend.repository;

import org.example.backend.entity.SeatClass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {

    List<SeatClass> findByType(String type);
}
