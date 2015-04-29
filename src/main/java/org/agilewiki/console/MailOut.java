package org.agilewiki.console;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * SSL sender.
 */
public class MailOut {
    public static boolean send(String toAddress, String subject, String body) {

        final String username = "laforge49@gmail.com";
        final String password = "Frankly#2";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setFrom(new InternetAddress("laforge49@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);

            return true;

        } catch (AuthenticationFailedException e) {
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}