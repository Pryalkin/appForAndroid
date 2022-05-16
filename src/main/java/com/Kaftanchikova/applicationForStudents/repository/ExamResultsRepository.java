package com.Kaftanchikova.applicationForStudents.repository;

import com.Kaftanchikova.applicationForStudents.entity.ExamRegistration;
import com.Kaftanchikova.applicationForStudents.entity.ExamResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultsRepository extends JpaRepository<ExamResults, Long> {

    List<ExamResults> findByExamRegistrationUserUsername(String username);
    Optional<ExamResults> findByExamRegistration(ExamRegistration examRegistration);
}
