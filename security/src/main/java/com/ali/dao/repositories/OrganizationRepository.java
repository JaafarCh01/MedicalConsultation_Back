package com.ali.dao.repositories;

import com.ali.dao.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OrganizationRepository extends JpaRepository<Organization,Integer> {
}
