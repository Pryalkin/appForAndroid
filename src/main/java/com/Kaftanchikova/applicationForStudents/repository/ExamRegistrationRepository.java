package com.Kaftanchikova.applicationForStudents.repository;

import com.Kaftanchikova.applicationForStudents.entity.ExamRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {

    Optional<ExamRegistration> findByUserUsernameAndItem(String username, String item);
    List<ExamRegistration> findByUserUsername(String username);
}
