package org.example.backend.entity;

import jakarta.persistence.*;

@Table(name = "Employee")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID", nullable = false)
    private Long employeeID;

    @Column(name = "First_Name", nullable = false)
    private String firstName;

    @Column(name = "Last_Name", nullable = false)
    private String lastName;

    @Column(name = "Gender", nullable = false)
    private char gender;

    @Column(name = "Date_of_Birth", nullable = false)
    private String dateOfBirth;
    // Check if dateOfBirth is in the past
    // Check if dateOfBirth is in the format dd-mm-yyyy

    @Column(name = "Age", nullable = false)
    private int age; // Check if age >= 18 and <= 100

    @Column(name = "Role", nullable = false)
    private String role;

    @Column(name = "Salary", nullable = false)
    private double salary; // Check if salary >= 0

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "Phone_Number", nullable = false)
    private String phoneNumber; // Check if phoneNumber is in the format 0xxxxxxxxx

    public Long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
