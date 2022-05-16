package com.Kaftanchikova.applicationForStudents.repository;

import com.Kaftanchikova.applicationForStudents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);
    User findUserByEmail(String email);
    Optional<User> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

}
