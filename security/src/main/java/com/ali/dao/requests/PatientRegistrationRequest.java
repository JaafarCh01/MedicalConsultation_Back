package com.ali.dao.requests;

import java.time.LocalDate;

public class PatientRegistrationRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String city;
    private String medicalHistory;

    // Add getters for all fields
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getMedicalHistory() { return medicalHistory; }

    // Add setters if needed
}