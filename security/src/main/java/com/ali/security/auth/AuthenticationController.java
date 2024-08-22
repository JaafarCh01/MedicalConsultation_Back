package com.ali.security.auth;

import com.ali.security.handler.BusinessErrorCodes;
import com.ali.security.handler.UserNotEnabledException;
import com.ali.security.security.JwtService;
import com.ali.security.user.User;
import com.ali.security.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register/doctor")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerDoctor(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {

        service.registerDoctor(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/patient")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerPatient(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.registerPatient(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/register/organization")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerOrganization(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.registerOrganization(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        try {
            // Load the user by email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            // Check if the user is enabled
            if (!user.isEnabled()) {
                // User is not enabled, call checkUserEnable method
                service.enableUser(user);
            }
            // User is enabled, proceed with authentication
            return ResponseEntity.ok(service.authenticate(request));
        } catch (UserNotEnabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @GetMapping("/activate-account")
    public ResponseEntity<String> confirm(@RequestParam String token) {
        try {
            service.activateAccount(token);
            return ResponseEntity.ok("Account activated successfully.");
        } catch (Exception e) {
            // Log exception for debugging
            System.err.println("Error during account activation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                            @Valid @RequestBody PasswordChangingRequest request){
        // Extract the JWT token from the Authorization header
        String token = authorizationHeader.replace("Bearer ", "");
        // Extract user email from the token
        String userEmail = jwtService.extractUsername(token);
        authenticationService.passwordChanging(userEmail,request);
        return ResponseEntity.ok("Password Changed Successfully");
    }
}