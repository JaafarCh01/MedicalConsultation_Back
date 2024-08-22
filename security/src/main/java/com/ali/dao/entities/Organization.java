package com.ali.dao.entities;

import com.ali.security.role.Role;
import com.ali.security.user.Gender;
import com.ali.security.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("Organization")
public class Organization extends User {

    private String organizationName;
    private String typeOfInstitution;

    @Lob
    private String description;
    private String facilityCity;
    private String facilityAddress;
    private String phoneNumber;
    private String schedule;
    private String website;
    private String facilityEmailAddress;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<HistoryOperations> historyOperations;

    @Builder
    public Organization(Integer id, String firstName, String lastName, LocalDate dateOfBirth,
                        String email, String password, String city, List<Role> roles, boolean accountLocked,
                        boolean enabled, LocalDate createdDate, LocalDate lastModifiedDate,
                        String organizationName, String typeOfInstitution, String description,
                        String facilityCity, String facilityAddress, String phoneNumber,
                        String schedule, String website, String facilityEmailAddress) {
        super(id, firstName, lastName, dateOfBirth, email, password, city, roles, accountLocked,
                enabled, createdDate, lastModifiedDate);
        this.organizationName = organizationName;
        this.typeOfInstitution = typeOfInstitution;
        this.description = description;
        this.facilityCity = facilityCity;
        this.facilityAddress = facilityAddress;
        this.phoneNumber = phoneNumber;
        this.schedule = schedule;
        this.website = website;
        this.facilityEmailAddress = facilityEmailAddress;
    }
}

