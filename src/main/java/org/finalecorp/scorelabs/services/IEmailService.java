package org.finalecorp.scorelabs.services;

import org.finalecorp.scorelabs.requestObjects.EmailContent;

public interface IEmailService {
    String sendSimpleMail(EmailContent details);

}