package com.ali.dao.requests;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
}
