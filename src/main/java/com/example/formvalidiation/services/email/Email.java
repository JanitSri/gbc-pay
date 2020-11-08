/**********************************************************************************
 * Project: GBC PAY - The Raptors
 * Assignment: Assignment 2
 * Author(s): Janit Sriganeshaelankovan, Shelton D'mello, Saif Bakhtaria
 * Student Number: 101229102, 101186743, 101028504
 * Date: November 08, 2020
 * Description: Interface for constructing emails.
 *********************************************************************************/

package com.example.formvalidiation.services.email;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface Email<T extends Token> {
    MimeMessage constructMessage(User user, T token) throws MessagingException;
}
