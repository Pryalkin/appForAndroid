package com.Kaftanchikova.applicationForStudents.service.impl;

import ch.qos.logback.core.boolex.EvaluationException;
import com.Kaftanchikova.applicationForStudents.entity.ExamRegistration;
import com.Kaftanchikova.applicationForStudents.entity.ExamResults;
import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.exception.domain.ExamFoundException;
import com.Kaftanchikova.applicationForStudents.repository.ExamRegistrationRepository;
import com.Kaftanchikova.applicationForStudents.repository.ExamResultsRepository;
import com.Kaftanchikova.applicationForStudents.repository.UserRepository;
import com.Kaftanchikova.applicationForStudents.service.ExamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.Kaftanchikova.applicationForStudents.constants.ExceptionConstant.EXAM_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRegistrationRepository examRegistrationRepository;
    private final ExamResultsRepository examResultsRepository;
    private final UserRepository userRepository;

    @Override
    public ExamRegistration registrationExam(ExamRegistration examRegistration, String username) throws ExamFoundException {
        validateExam(username, examRegistration.getItem());
        examRegistration.setUser(userRepository.findUserByUsername(username));
        examRegistration.setRegistrationDate(new Date());
        return examRegistrationRepository.save(examRegistration);
    }

    @Override
    public ExamResults registrationItem(String item, String grade, User user) throws EvaluationException {
        User findUser = userRepository.findByFirstNameAndMiddleNameAndLastName(user.getFirstName(), user.getMiddleName(), user.getLastName()).get();
        ExamRegistration  examRegistration = examRegistrationRepository.findByUserUsernameAndItem(findUser.getUsername(), item).get();
        ExamResults examResults = new ExamResults();
        if (examResultsRepository.findByExamRegistration(examRegistration).isPresent()){
            throw new EvaluationException("This student has already been graded");
        }
        examResults.setExamRegistration(examRegistration);
        examResults.setGrade(Integer.parseInt(grade));
        return examResultsRepository.save(examResults);
    }
    @Override
    public List<ExamResults> examResults(String username) {
        return examResultsRepository.findByExamRegistrationUserUsername(username);
    }

    @Override
    public List<ExamRegistration> examRegistrationResults(String username) {
        return examRegistrationRepository.findByUserUsername(username);
    }

    @Override
    public void deleteExam(String item, String username) {
        ExamRegistration examRegistration = examRegistrationRepository.findByUserUsernameAndItem(username, item).get();
        examRegistrationRepository.deleteById(examRegistration.getId());
    }

    private void validateExam(String username, String item) throws ExamFoundException {
        if (examRegistrationRepository.findByUserUsernameAndItem(username, item).isPresent()){
            throw new ExamFoundException(EXAM_FOUND_EXCEPTION);
        }
    }
}
