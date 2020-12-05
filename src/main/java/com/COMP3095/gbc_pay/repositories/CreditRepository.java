/* *********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 3
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: December 05, 2020
 * Description: Repository for Credit entity, used for CRUD operations of Credit.
 ******************************************************************************** */

package com.COMP3095.gbc_pay.repositories;


import com.COMP3095.gbc_pay.models.Credit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Long> {
}
