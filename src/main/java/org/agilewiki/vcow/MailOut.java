package org.agilewiki.vcow;

import org.agilewiki.jactor2.core.blades.BlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.util.Date;
import java.util.Properties;

/**
 * SSL sender.
 */
public class MailOut extends BlockingBladeBase {
    static Session session;

    static {
        final String username = "laforge49@gmail.com";
        final String password = "Frankly#2";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static boolean send(String toAddress, String subject, String body)
            throws MessagingException {


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
        }
    }

    public MailOut() throws Exception {
    }

    public AReq<Boolean> sendEmail(ServletContext servletContext,
                                   String toAddress,
                                   String subject,
                                   String body,
                                   StringBuilder errorBuilder) {
        return new AReq<Boolean>("sendEmail") {
            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Boolean> _asyncResponseProcessor)
                    throws Exception {
                boolean success = false;
                try {
                    success = MailOut.this.send(toAddress, subject, body);
                    if (!success) {
                        servletContext.log("email authentication error");
                        if (errorBuilder != null) {
                            errorBuilder.append(
                                    "Unable to send an email to your address at this time. Please try again later.");
                        }
                    }
                } catch (MessagingException e) {
                    if (errorBuilder != null) {
                        errorBuilder.append(e.getMessage());
                    }
                }
                _asyncResponseProcessor.processAsyncResponse(success);
            }
        };
    }
}
