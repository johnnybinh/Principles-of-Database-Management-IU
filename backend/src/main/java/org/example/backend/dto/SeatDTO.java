package org.example.backend.dto;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class SeatDTO {
    private String class_type;

    public SeatDTO(String class_type) {
        this.class_type = class_type;
    }
}
