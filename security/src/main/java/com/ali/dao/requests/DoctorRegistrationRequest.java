package com.ali.dao.requests;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DoctorRegistrationRequest {

    @NotBlank(message = "Specialization is mandatory")
    private String speciality;

    @NotBlank(message = "Education details are mandatory")
    private String education;

    @NotBlank(message = "Current place of work is mandatory")
    private String workPlace;

    @NotBlank(message = "Position is mandatory")
    private String position;

    @Min(value = 0, message = "Work experience must be a positive number")
    private int workExperienceYears;

    private String awards;

    //@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number")
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;

    @Size(max = 500, message = "About me section cannot exceed 500 characters")
    private String aboutMe;

    @Size(max = 500, message = "Specialization details cannot exceed 500 characters")
    private String specializationDetails;

    @Size(max = 500, message = "Work experience details cannot exceed 500 characters")
    private String workExperienceDetails;

    @Size(max = 500, message = "Further training details cannot exceed 500 characters")
    private String furtherTraining;

    @Size(max = 500, message = "Achievements and awards details cannot exceed 500 characters")
    private String achievementsAndAwards;

    @Size(max = 500, message = "Scientific works details cannot exceed 500 characters")
    private String scientificWorks;

    @NotNull(message = "Profile image is required")
    private MultipartFile profileImage;

    @NotEmpty(message = "At least one certificate must be uploaded")
    private List<MultipartFile> certificates;
}

