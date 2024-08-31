package com.ali.security.config;

import com.ali.dao.entities.Appointment;
import com.ali.dao.entities.Doctor;
import com.ali.dao.entities.Patient;
import com.ali.dao.repositories.AppointmentRepository;
import com.ali.dao.repositories.DoctorRepository;
import com.ali.dao.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DoctorRepository doctorRepository,
                                   PatientRepository patientRepository,
                                   AppointmentRepository appointmentRepository) {
        return args -> {
            // Check if we already have appointments for doctor with ID 7
            if (appointmentRepository.findByDoctorId(7).isEmpty()) {
                // Find the doctor with ID 7
                Optional<Doctor> doctorOpt = doctorRepository.findById(7);
                Doctor doctor;
                if (doctorOpt.isPresent()) {
                    doctor = doctorOpt.get();
                } else {
                    // If doctor with ID 7 doesn't exist, create a new one
                    doctor = Doctor.builder()
                        .id(7)
                        .firstName("John")
                        .lastName("Doe")
                        .speciality("Cardiology")
                        .build();
                    doctorRepository.save(doctor);
                }

                // Create patients
                Patient patient1 = Patient.builder()
                    .firstName("Alice")
                    .lastName("Smith")
                    .build();
                Patient patient2 = Patient.builder()
                    .firstName("Bob")
                    .lastName("Johnson")
                    .build();
                patientRepository.saveAll(List.of(patient1, patient2));

                // Create appointments for doctor with ID 7
                Appointment appointment1 = Appointment.builder()
                    .doctor(doctor)
                    .patient(patient1)
                    .appointmentDateTime(LocalDateTime.now().plusDays(1))
                    .status("Scheduled")
                    .build();
                Appointment appointment2 = Appointment.builder()
                    .doctor(doctor)
                    .patient(patient2)
                    .appointmentDateTime(LocalDateTime.now().plusDays(2))
                    .status("Scheduled")
                    .build();
                appointmentRepository.saveAll(List.of(appointment1, appointment2));
            }
        };
    }
}