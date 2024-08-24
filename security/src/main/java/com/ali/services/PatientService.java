package com.ali.services;

import com.ali.dao.entities.Patient;
import com.ali.dao.requests.PatientRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient saveOrUpdatePatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(Integer id);
    void deletePatientById(Integer id);
    Patient registerPatient(String userEmail, PatientRegistrationRequest request);
    Patient updatePatient(Integer id, PatientRegistrationRequest request);
}