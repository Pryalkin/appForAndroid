package com.Kaftanchikova.applicationForStudents.service;

import ch.qos.logback.core.boolex.EvaluationException;
import com.Kaftanchikova.applicationForStudents.entity.ExamRegistration;
import com.Kaftanchikova.applicationForStudents.entity.ExamResults;
import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.exception.domain.ExamFoundException;

import java.util.List;

public interface ExamService {
    ExamRegistration registrationExam(ExamRegistration examRegistration, String username) throws ExamFoundException;
    ExamResults registrationItem(String item, String grade, User user) throws EvaluationException;
    List<ExamResults> examResults(String username);
    List<ExamRegistration> examRegistrationResults(String username);
    void deleteExam(String item, String username);
}
