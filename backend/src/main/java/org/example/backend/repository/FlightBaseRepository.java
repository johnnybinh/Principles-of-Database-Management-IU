package org.example.backend.repository;

import org.example.backend.entity.FlightBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBaseRepository extends JpaRepository<FlightBase, Long> {}
