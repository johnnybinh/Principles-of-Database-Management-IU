package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerDTO {

    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String phoneNumber;
    private String passportNumber;
    private String dateOfBirth;
    private String nationality;
    private String address;

    public PassengerDTO(String firstName, String lastName, int age,
                        String email, String phoneNumber, String passportNumber,
                        String dateOfBirth, String nationality, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.address = address;
    }

}

