package com.ali.web;


import com.ali.dao.requests.OrganizationRegistrationRequest;
import com.ali.security.security.JwtService;
import com.ali.services.OrganizationService;
import com.ali.dao.entities.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final JwtService jwtService;

    @PostMapping("/registerOrganization")
    public ResponseEntity<?> registerOrganization(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody OrganizationRegistrationRequest request) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            String userEmail = jwtService.extractUsername(token);
            Organization organization = organizationService.registerOrganization(userEmail, request);
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering organization: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrganizations() {
        try {
            List<Organization> organizations = organizationService.getAllOrganization();
            return ResponseEntity.ok(organizations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching organizations: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Integer id) {
        try {
            return organizationService.getOrganizationById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching organization: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Integer id) {
        try {
            organizationService.deleteOrganization(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting organization: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Integer id, @RequestBody OrganizationRegistrationRequest request) {
        try {
            Organization updatedOrganization = organizationService.updateOrganization(id, request);
            return ResponseEntity.ok(updatedOrganization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating organization: " + e.getMessage());
        }
    }
}