package com.ali.dao.repositories;

import com.ali.dao.entities.Certificate;
import com.ali.dao.entities.HistoryOperations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryOperationsRepository extends JpaRepository<HistoryOperations, Integer> {
}
