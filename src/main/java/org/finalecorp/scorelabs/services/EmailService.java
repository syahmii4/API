package org.finalecorp.scorelabs.services;

import java.util.Properties;
import javax.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.finalecorp.scorelabs.requestObjects.EmailContent;

public class EmailService implements IEmailService {

    private String sender = "scorelabsmoderation@gmail.com";
    private String password = "msgbtraqmsqizpyj";
    private String host = "smtp.gmail.com";
    private String port = "587";

    public String sendSimpleMail(EmailContent details) {
        try {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            //create the Session object
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                }
            );

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(details.getRecipient()));
            message.setSubject(details.getSubject());
            message.setText(details.getMsgBody());
            Transport.send(message);

            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            System.out.println(e);
            return "Error while Sending Mail";
        }
    }

    public String getProps() {
        return(sender + " " + password);
    }
}