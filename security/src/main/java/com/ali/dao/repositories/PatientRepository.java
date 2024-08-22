package com.ali.dao.repositories;

import com.ali.dao.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PatientRepository extends JpaRepository<Patient,Integer> {
}
