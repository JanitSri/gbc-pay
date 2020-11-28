package com.COMP3095.gbc_pay.repositories;

import com.COMP3095.gbc_pay.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndDateOfBirth(String firstName, String lastName,
                                                                                LocalDate dateOfBirth);
}
