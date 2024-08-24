package com.ali.services;

import com.ali.dao.entities.Question;
import java.util.List;
import java.util.Optional;

public interface QuestionService {
    Question saveQuestion(Question question);
    List<Question> getAllQuestions();
    Optional<Question> getQuestionById(Long id);
    void deleteQuestionById(Long id);
}