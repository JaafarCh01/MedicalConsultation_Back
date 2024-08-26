package com.ali.security.auth;

import com.ali.dao.entities.Doctor;
import com.ali.dao.entities.Organization;
import com.ali.dao.entities.Patient;
import com.ali.dao.requests.OrganizationRegistrationRequest;
import com.ali.security.email.EmailService;
import com.ali.security.email.EmailTemplateName;
import com.ali.security.handler.EmailAlreadyExistsException;
import com.ali.security.handler.UserNotEnabledException;
import com.ali.security.role.RoleRepository;
import com.ali.security.security.JwtService;
import com.ali.security.user.Token;
import com.ali.security.user.TokenRepository;
import com.ali.security.user.User;
import com.ali.security.user.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void registerDoctor(RegistrationRequest request) throws MessagingException {
        registerUser(request, "DOCTOR");
    }

    public void registerPatient(RegistrationRequest request) throws MessagingException {
        registerUser(request, "PATIENT");
    }

    public void registerOrganization(OrganizationRegistrationRequest request) throws MessagingException {
        registerOrganizationUser(request);
    }

    private void registerOrganizationUser(OrganizationRegistrationRequest request) throws MessagingException {
        var role = roleRepository.findByName("ORGANIZATION")
                .orElseThrow(() -> new IllegalStateException("ROLE ORGANIZATION NOT FOUND"));

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        Organization organization = Organization.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(role))
                .dateOfBirth(request.getDateOfBirth())
                .city(request.getCity())
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

        userRepository.save(organization);
        sendValidationEmail(organization);
    }

    private void registerUser(RegistrationRequest request, String roleName) throws MessagingException {
        var role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("ROLE " + roleName + " NOT FOUND"));

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user;

        switch (roleName) {
            case "DOCTOR":
                user = Doctor.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .accountLocked(false)
                        .enabled(false)
                        .roles(List.of(role))
                        .dateOfBirth(request.getDateOfBirth())
                        .city(request.getCity())
                        .build();
                break;
            case "PATIENT":
                user = Patient.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .accountLocked(false)
                        .enabled(false)
                        .roles(List.of(role))
                        .dateOfBirth(request.getDateOfBirth())
                        .city(request.getCity())
                        .build();
                break;
            case "ORGANIZATION":
                user = Organization.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .accountLocked(false)
                        .enabled(false)
                        .roles(List.of(role))
                        .dateOfBirth(request.getDateOfBirth())
                        .city(request.getCity())
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + roleName);
        }
        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void passwordChanging(String userEmail, PasswordChangingRequest request) {
        // Find Connected User
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if old Password and User's Password Match
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new IllegalArgumentException("New password matches the old one");
        }

        // Check if both passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if old password matches user's password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password incorrect");
        }

        // Encode the new password and update it
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
    }


    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        System.out.println("Generated Activation Token: " + newToken);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        System.out.println("Generated Token: " + generatedToken);
        System.out.println("Token Details: " + token);
        try {
            tokenRepository.save(token);
            System.out.println("Token saved to database.");
        } catch (Exception e) {
            System.err.println("Error saving token to database: " + e.getMessage());
        }
        return generatedToken;
    }



    /**
     * This method generates a code to be sent by mail
     */
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .user(com.ali.security.user.UserDTO.fromUser(user))  // Add this line to include user information
            .build();
    }

    public void enableUser(User user){
            // Send validation email if not enabled
        try {
            sendValidationEmail(user);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send validation email", e);
        }
        throw new UserNotEnabledException("User account is not enabled. Activation email has been sent.");
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    System.err.println("Token not found: " + token); // Log token issue
                    return new RuntimeException("Invalid Token");
                });

        System.out.println("Retrieved Token: " + savedToken.getToken()); // Log token details

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new Token has been sent");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> {
                    System.err.println("User not found for token: " + savedToken.getToken()); // Log user issue
                    return new UsernameNotFoundException("User Not Found");
                });

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

}