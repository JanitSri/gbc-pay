package com.COMP3095.gbc_pay.services.email;

import com.COMP3095.gbc_pay.models.Profile;
import com.COMP3095.gbc_pay.models.Token;
import com.COMP3095.gbc_pay.models.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface Email<T extends Token> {
    MimeMessage constructMessage(User user, Profile profile, T token) throws MessagingException;
}
