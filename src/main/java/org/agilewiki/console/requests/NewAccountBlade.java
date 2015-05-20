package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.Tokens;
import org.agilewiki.console.User;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;

import javax.servlet.AsyncContext;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class NewAccountBlade extends PostRequestBlade {
    public NewAccountBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                String error = null;
                String emailAddress = request.getParameter("emailAddress2");
                if (emailAddress != null)
                    map.put("emailAddress2", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                if (emailAddress == null || emailAddress.length() == 0)
                    error = "Email address is required";
                if (error != null) {
                    map.put("error2", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                }
                String userId = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String subject = null;
                String body = null;
                if (userId == null) {
                    try {
                        String token = Tokens.generate(db, emailAddress,
                                1000 * 60 * 60 * 24 + System.currentTimeMillis()); //1 day validity
                        subject = "Address Validation Request";
                        body = "<p>To validate your email address and begin opening an account, please click " +
                                "<a href=\"" + SimpleSimon.self + "?to=validated&email=" + emailAddress +
                                "&key=" + token + "\">here</a>.</p>" +
                                "<p>--Virtual Cow</p>";
                    } catch (NoSuchAlgorithmException e) {
                        servletContext.log("no such algorithm: SHA-256");
                        map.put("success2", SimpleSimon.encode(
                                "Unable to send an email to your address at this time. Please try again later.",
                                0, SimpleSimon.ENCODE_FIELD)); //field
                        finish();
                        return;
                    }
                } else {
                    subject = "New Account Notification";
                    body = "<p>An attempt was made to open another account with your email address.</p>" +
                            "<p>--Virtual Cow</p>";
                }
                asyncRequestImpl.send(mailOut.sendEmail(servletContext, emailAddress, subject, body, null),
                        new AsyncResponseProcessor<Boolean>() {
                            @Override
                            public void processAsyncResponse(Boolean _response) throws Exception {
                                if (_response)
                                    map.put("success2", SimpleSimon.encode(
                                            "An email has been sent to verify your address. Please check your inbox.",
                                            0, SimpleSimon.ENCODE_FIELD)); //field
                                else
                                    map.put("success2", SimpleSimon.encode(
                                            "Unable to send an email to your address at this time. Please try again later.",
                                            0, SimpleSimon.ENCODE_FIELD)); //field
                                finish();
                            }
                        });
            }
        }.signal();
    }
}
