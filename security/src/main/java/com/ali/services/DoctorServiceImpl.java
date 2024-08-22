package com.ali.services;


import com.ali.dao.entities.Certificate;
import com.ali.dao.entities.Doctor;
import com.ali.dao.repositories.DoctorRepository;
import com.ali.dao.requests.DoctorRegistrationRequest;
import com.ali.security.user.User;
import com.ali.security.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    public Doctor saveOrUpdateDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Integer id){
        return doctorRepository.findById(id);
    }

    @Override
    public void deleteDoctorById(Integer id){
        doctorRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Doctor registerDoctor(String userEmail, DoctorRegistrationRequest request) throws IOException {
        // Find the User by email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert MultipartFile to byte[]
        byte[] profileImageBytes = request.getProfileImage() != null ? request.getProfileImage().getBytes() : null;

        // Create a Doctor entity
        Doctor doctor = Doctor.builder()
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
                .speciality(request.getSpeciality())
                .education(request.getEducation())
                .workPlace(request.getWorkPlace())
                .position(request.getPosition())
                .workExperienceYears(request.getWorkExperienceYears())
                .awards(request.getAwards())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .aboutMe(request.getAboutMe())
                .specializationDetails(request.getSpecializationDetails())
                .workExperienceDetails(request.getWorkExperienceDetails())
                .furtherTraining(request.getFurtherTraining())
                .achievementsAndAwards(request.getAchievementsAndAwards())
                .scientificWorks(request.getScientificWorks())
                .profileImage(profileImageBytes)
                .build();

        // Set Doctor reference in Certificates
        List<Certificate> certificateList = request.getCertificates() != null ?
                request.getCertificates().stream().map(file -> {
                    try {
                        Certificate cert = Certificate.builder()
                                .certificateName(file.getOriginalFilename())
                                .certificateFile(file.getBytes())
                                .doctor(doctor) // Set the doctor reference
                                .build();
                        return cert;
                    } catch (IOException e) {
                        throw new RuntimeException("Error processing certificate file", e);
                    }
                }).collect(Collectors.toList()) : Collections.emptyList();

        doctor.setCertificates(certificateList); // Set certificates in doctor

        // Save the Doctor entity
        return doctorRepository.save(doctor);
    }


}
