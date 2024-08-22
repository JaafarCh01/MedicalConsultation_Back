package com.ali.web;


import com.ali.dao.requests.OrganizationRegistrationRequest;
import com.ali.security.security.JwtService;
import com.ali.services.OrganizationService;
import com.ali.dao.entities.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final JwtService jwtService;

    // To test the endpoint
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        System.out.println("Ping endpoint called");
        return ResponseEntity.ok("Pong");
    }

    @PostMapping("/registerOrganization")
    public ResponseEntity<Organization> registerOrganization(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody OrganizationRegistrationRequest request) {

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Extract user email from the token
        String userEmail = jwtService.extractUsername(token);

        // Register the organization
        Organization organization = organizationService.registerOrganization(userEmail, request);

        return ResponseEntity.ok(organization);
    }
}

