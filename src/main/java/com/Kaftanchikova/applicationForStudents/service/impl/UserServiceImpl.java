package com.Kaftanchikova.applicationForStudents.service.impl;

import com.Kaftanchikova.applicationForStudents.entity.UserPrincipal;
import com.Kaftanchikova.applicationForStudents.exception.domain.EmailExistException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UserNotFoundException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UsernameExistException;
import com.Kaftanchikova.applicationForStudents.repository.UserRepository;
import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.service.LoginAttemptService;
import com.Kaftanchikova.applicationForStudents.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;

import static com.Kaftanchikova.applicationForStudents.constants.FileConstant.*;
import static com.Kaftanchikova.applicationForStudents.constants.UserImplConstant.*;
import static com.Kaftanchikova.applicationForStudents.enumeration.Role.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@Qualifier("userDetailsService")
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            log.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(user);
//            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info(FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User registerAnApplicant(String firstName, String middleName, String lastName, String username) {
        User applicant = userRepository.findUserByUsername(username);
        applicant.setFirstName(firstName);
        applicant.setMiddleName(middleName);
        applicant.setLastName(lastName);
        applicant.setRole(ROLE_APPLICANT.name());
        applicant.setAuthorities(ROLE_APPLICANT.getAuthorities());
        return userRepository.save(applicant);
    }

    @Override
    public User register(String username, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(EMPTY, username, email);
        User user = new User();
        user.setUserId(generateUserId());
        String encodedPassword = encodePassword(password);
        user.setFirstName(null);
        user.setLastName(null);
        user.setMiddleName(null);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        if (userRepository.findAll().isEmpty()){
            user.setRole(ROLE_SUPER_ADMIN.name());
            user.setAuthorities(ROLE_SUPER_ADMIN.getAuthorities());
        } else {
            user.setRole(ROLE_USER.name());
            user.setAuthorities(ROLE_USER.getAuthorities());
        }
        user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
        userRepository.save(user);
        log.info("New user password: " + password);
//        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()){
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserByUsername(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }
}
