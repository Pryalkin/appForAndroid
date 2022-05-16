package com.Kaftanchikova.applicationForStudents.controller;

import com.Kaftanchikova.applicationForStudents.entity.User;
import com.Kaftanchikova.applicationForStudents.entity.UserPrincipal;
import com.Kaftanchikova.applicationForStudents.exception.ExceptionHandling;
import com.Kaftanchikova.applicationForStudents.exception.domain.EmailExistException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UserNotFoundException;
import com.Kaftanchikova.applicationForStudents.exception.domain.UsernameExistException;
import com.Kaftanchikova.applicationForStudents.service.UserService;
import com.Kaftanchikova.applicationForStudents.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.Kaftanchikova.applicationForStudents.constants.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/", "/user"})
@Slf4j
@AllArgsConstructor
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException{
        User newUser = userService.register(user.getUsername(), user.getEmail(), user.getPassword());
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/registrationForApplicant/{username}")
    public ResponseEntity<User> register(@PathVariable String username,
                                         @RequestBody User user) {
        return new ResponseEntity<>(userService.registerAnApplicant(user.getFirstName(), user.getMiddleName(), user.getLastName(), username), OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
