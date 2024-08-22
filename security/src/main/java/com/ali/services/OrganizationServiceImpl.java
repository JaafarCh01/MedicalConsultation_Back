package com.ali.services;


import com.ali.dao.entities.Organization;
import com.ali.dao.requests.OrganizationRegistrationRequest;
import com.ali.dao.repositories.OrganizationRepository;
import com.ali.security.user.User;
import com.ali.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;


    @Override
    public Organization saveOrUpdateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAllOrganization() {
        return organizationRepository.findAll();
    }

    @Override
    public Optional<Organization> getOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    @Override
    public void deleteOrganization(Integer id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public Organization registerOrganization(String userEmail, OrganizationRegistrationRequest request) {
        // Find the User by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and populate the Organization entity
        Organization organization = Organization.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .password(user.getPassword())
                .city(user.getCity())
                .roles(user.getRoles())
                .accountLocked(user.isAccountNonLocked())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                //From the JSON REQUEST
                .organizationName(request.getOrganizationName())
                .typeOfInstitution(request.getTypeOfInstitution())
                .description(request.getDescription())
                .facilityCity(request.getFacilityCity())
                .facilityAddress(request.getFacilityAddress())
                .phoneNumber(request.getPhoneNumber())
                .schedule(request.getSchedule())
                .website(request.getWebsite())
                .facilityEmailAddress(request.getFacilityEmailAddress())
                .build();

        // Save the Organization entity
        return organizationRepository.save(organization);
    }
}

