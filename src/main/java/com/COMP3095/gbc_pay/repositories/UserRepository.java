/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
* Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
* Student Number: 101229102, 101186743, 101028504
* Date: December 05, 2020
* Description: Repository for User entity, used for CRUD operations of Users.
 ******************************************************************************** */

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
