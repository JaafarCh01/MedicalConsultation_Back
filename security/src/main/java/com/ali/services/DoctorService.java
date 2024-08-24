package com.ali.services;



import com.ali.dao.entities.Doctor;
import com.ali.dao.entities.Organization;
import com.ali.dao.requests.DoctorRegistrationRequest;
import com.ali.dao.requests.OrganizationRegistrationRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor saveOrUpdateDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Optional<Doctor> getDoctorById(Integer id);
    void deleteDoctorById(Integer id);

    Doctor registerDoctor(String userEmail, DoctorRegistrationRequest request) throws IOException;
    Doctor updateDoctor(Integer id, DoctorRegistrationRequest request) throws IOException;
}