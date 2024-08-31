package com.ali.dao.entities;

import com.ali.security.role.Role;
import com.ali.security.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@DiscriminatorValue("Doctor")
public class Doctor extends User {

    private String speciality;
    private String education;
    private String workPlace;
    private String position;
    private int workExperienceYears;
    private String awards;
    private String contactPhone;
    private String contactEmail;
    private String aboutMe;
    private String specializationDetails;
    private String workExperienceDetails;
    private String furtherTraining;
    private String achievementsAndAwards;
    private String scientificWorks;

    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] profileImage;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Answer> answers;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HistoryOperations> historyOperations;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(
        name = "doctor_patient",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    private List<Patient> patients;

    @Builder
    public Doctor(Integer id, String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password, String city, List<Role> roles, boolean accountLocked,
                  boolean enabled, LocalDate createdDate, LocalDate lastModifiedDate,
                  String speciality, String education, String workPlace, String position, int workExperienceYears,
                  String awards, String contactPhone, String contactEmail,
                  String aboutMe, String specializationDetails,
                  String workExperienceDetails, String furtherTraining, String achievementsAndAwards, String scientificWorks,
                  byte[] profileImage, List<Certificate> certificates) {
        super(id, firstName, lastName, dateOfBirth, email, password, city,  roles, accountLocked,
                enabled, createdDate, lastModifiedDate);
        this.speciality = speciality;
        this.education = education;
        this.workPlace = workPlace;
        this.position = position;
        this.workExperienceYears = workExperienceYears;
        this.awards = awards;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.aboutMe = aboutMe;
        this.specializationDetails = specializationDetails;
        this.workExperienceDetails = workExperienceDetails;
        this.furtherTraining = furtherTraining;
        this.achievementsAndAwards = achievementsAndAwards;
        this.scientificWorks = scientificWorks;
        this.profileImage = profileImage;
        this.certificates = certificates;
    }
}
