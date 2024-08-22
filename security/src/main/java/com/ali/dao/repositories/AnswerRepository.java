package com.ali.dao.repositories;

import com.ali.dao.entities.Answer;
import com.ali.dao.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
