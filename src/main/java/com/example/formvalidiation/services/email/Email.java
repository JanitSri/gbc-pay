package com.example.formvalidiation.services.email;

import com.example.formvalidiation.models.Token;
import com.example.formvalidiation.models.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface Email<T extends Token> {
    MimeMessage constructMessage(User user, T token) throws MessagingException;
}
