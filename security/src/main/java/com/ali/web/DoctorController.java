package com.ali.web;

import com.ali.dao.requests.DoctorRegistrationRequest;
import com.ali.security.security.JwtService;
import com.ali.services.DoctorService;

import com.ali.dao.entities.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final JwtService jwtService;

    @PostMapping("/registerDoctor")
    public ResponseEntity<Doctor> registerDoctor(
            @RequestHeader("Authorization") String authorizationHeader,
            @ModelAttribute DoctorRegistrationRequest request) throws IOException {

        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");

        // Extract user email from the token
        String userEmail = jwtService.extractUsername(token);

        // Register the doctor
        Doctor doctor = doctorService.registerDoctor(userEmail, request);

        return ResponseEntity.ok(doctor);
    }


}
