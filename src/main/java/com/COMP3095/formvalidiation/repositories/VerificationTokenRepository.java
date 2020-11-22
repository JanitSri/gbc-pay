/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Repository for the working with verification token data and extends the token repository.
 *********************************************************************************/

package com.COMP3095.formvalidiation.repositories;


import com.COMP3095.formvalidiation.models.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository("verificationTokenRepository")
public interface VerificationTokenRepository extends TokenRepository<VerificationToken> {
}
