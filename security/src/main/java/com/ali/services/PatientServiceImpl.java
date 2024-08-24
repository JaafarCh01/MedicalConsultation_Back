package com.ali.services;

import com.ali.dao.entities.Patient;
import com.ali.dao.repositories.PatientRepository;
import com.ali.dao.requests.PatientRegistrationRequest;
import com.ali.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ali.security.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public Patient saveOrUpdatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Integer id) {
        return patientRepository.findById(id);
    }

    @Override
    public void deletePatientById(Integer id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Patient registerPatient(String userEmail, PatientRegistrationRequest request) {
        // Find the User by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a Patient entity
        Patient patient = Patient.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .password(user.getPassword())
                .city(user.getCity())
                .roles(user.getRoles())
                .accountLocked(user.isAccountLocked())
                .enabled(user.isEnabled())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .medicalHistory(request.getMedicalHistory())
                .build();

        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Integer id, PatientRegistrationRequest request) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update patient fields
        existingPatient.setFirstName(request.getFirstName());
        existingPatient.setLastName(request.getLastName());
        existingPatient.setDateOfBirth(request.getDateOfBirth());
        existingPatient.setGender(request.getGender());
        existingPatient.setPhoneNumber(request.getPhoneNumber());
        existingPatient.setAddress(request.getAddress());
        existingPatient.setCity(request.getCity());
        existingPatient.setMedicalHistory(request.getMedicalHistory());

        return patientRepository.save(existingPatient);
    }
}