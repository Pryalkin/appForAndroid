package com.Kaftanchikova.applicationForStudents.controller;

import ch.qos.logback.core.boolex.EvaluationException;
import com.Kaftanchikova.applicationForStudents.entity.ExamRegistration;
import com.Kaftanchikova.applicationForStudents.entity.ExamResults;
import com.Kaftanchikova.applicationForStudents.entity.HttpResponse;
import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.exception.ExceptionHandling;
import com.Kaftanchikova.applicationForStudents.exception.domain.ExamFoundException;
import com.Kaftanchikova.applicationForStudents.service.ExamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/exams")
@Slf4j
@AllArgsConstructor
public class ExamController extends ExceptionHandling {

    private final ExamService examService;

    @PostMapping("/register/{username}")
    public ResponseEntity<ExamRegistration> registrationExam(@PathVariable String username,
                                                             @RequestBody ExamRegistration examRegistration) throws ExamFoundException {
        ExamRegistration newExamRegistration = examService.registrationExam(examRegistration, username);
        return new ResponseEntity<>(newExamRegistration, OK);
    }

    @PostMapping("/register/{item}/{grade}")
    public ResponseEntity<ExamResults> registrationItem(@PathVariable String item,
                                                        @PathVariable String grade,
                                                        @RequestBody User user) throws ExamFoundException, EvaluationException {
        ExamResults newExamRegistration = examService.registrationItem(item, grade, user);
        return new ResponseEntity<>(newExamRegistration, OK);
    }

    @GetMapping("/results/grades/{username}")
    public ResponseEntity<List<ExamResults>> registrationExam(@PathVariable String username) {
        List<ExamResults> examRegistrationList = examService.examResults(username);
        return new ResponseEntity<>(examRegistrationList, OK);
    }

    @GetMapping("/resultRegister/{username}")
    public ResponseEntity<List<ExamRegistration>> examRegistrationResults(@PathVariable String username) {
        log.info("LOGIN");
        List<ExamRegistration> examRegistrationList = examService.examRegistrationResults(username);
        return new ResponseEntity<>(examRegistrationList, OK);
    }

    @GetMapping("/delete/register/{item}/{username}")
    public void deleteExam(@PathVariable String item,
                                                   @PathVariable String username) {
        log.info("I controller!!!");
        examService.deleteExam(item, username);
//        return response(OK, "Экзамен успешно удален!");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

}
