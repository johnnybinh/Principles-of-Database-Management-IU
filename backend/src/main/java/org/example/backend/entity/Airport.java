package org.example.backend.entity;

import jakarta.persistence.*;

@Table(name = "Airport")
@Entity
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AirportID", nullable = false)
    private Long airportID;

    @Column(name = "Airport_Name", nullable = false)
    private String airportName;

    @Column(name = "City", nullable = false)
    private String city;

    @Column(name = "Country", nullable = false)
    private String country;

    public Long getAirportID() {
        return airportID;
    }

    public void setAirportID(Long airportID) {
        this.airportID = airportID;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
