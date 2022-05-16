package com.Kaftanchikova.applicationForStudents.service;

import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.exception.domain.EmailExistException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UserNotFoundException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UsernameExistException;

public interface UserService {

    User findUserByUsername(String username);
    User register(String username, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException;
    User findUserByEmail(String email);
    User registerAnApplicant(String firstName, String middleName, String lastName, String username);
}
