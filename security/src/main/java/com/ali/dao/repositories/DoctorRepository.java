package com.ali.dao.repositories;

import com.ali.dao.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
