package com.ali.services;



import com.ali.dao.entities.Organization;
import com.ali.dao.requests.OrganizationRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Organization saveOrUpdateOrganization(Organization organization);
    List<Organization> getAllOrganization();
    Optional<Organization> getOrganizationById(Integer id);
    void deleteOrganization(Integer id);
    Organization registerOrganization(String userEmail, OrganizationRegistrationRequest request);
    Organization updateOrganization(Integer id, OrganizationRegistrationRequest request);
}