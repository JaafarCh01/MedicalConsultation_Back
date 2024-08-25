package com.ali.dao.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class OrganizationRegistrationRequest {
    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Type of institution is required")
    private String typeOfInstitution;

    @Size(min = 70, message = "Description must exceed 70 characters")
    private String description;

    @NotBlank(message = "Facility city is required")
    private String facilityCity;

    @NotBlank(message = "Facility address is required")
    private String facilityAddress;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String schedule;

    @Size(max = 100, message = "Website URL must not exceed 100 characters")
    private String website;

    @Email(message = "Facility email address must be a valid email address")
    private String facilityEmailAddress;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "City is required")
    private String city;
}