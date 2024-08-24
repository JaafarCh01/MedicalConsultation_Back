package com.ali.dao.entities;

import com.ali.security.role.Role;
import com.ali.security.user.Gender;
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
@DiscriminatorValue("Patient")
public class Patient extends User {

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Question> questions;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HistoryOperations> historyOperations;

    private String medicalHistory;
    private String gender;
    private String phoneNumber;
    @Setter
    private String address;

    @Builder
    public Patient(Integer id, String firstName, String lastName, LocalDate dateOfBirth,
                   String email, String password, String city, List<Role> roles, boolean accountLocked,
                   boolean enabled, LocalDate createdDate, LocalDate lastModifiedDate, String medicalHistory) {
        super(id, firstName, lastName, dateOfBirth, email, password, city, roles, accountLocked,
                enabled, createdDate, lastModifiedDate);
        this.medicalHistory = medicalHistory;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}