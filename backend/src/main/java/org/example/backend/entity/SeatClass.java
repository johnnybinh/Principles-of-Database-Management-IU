package org.example.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "seat_class")
@Entity
public class SeatClass {

    @Id
    @Column(name = "class_type", nullable = false)
    private String classType;

    @Column(name = "base_price", nullable = false)
    private float basePrice;
}
