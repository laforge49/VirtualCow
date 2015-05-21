package org.agilewiki.console.requests.profile;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.Tokens;
import org.agilewiki.console.User;
import org.agilewiki.console.requests.PostRequestBlade;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;

import javax.servlet.AsyncContext;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class ChangeEmailAddressBlade extends PostRequestBlade {
    public ChangeEmailAddressBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "profile";
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
                String emailAddress = request.getParameter("emailAddress");
                if (emailAddress != null)
                    map.put("emailAddress", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                String error = null;
                String oldEmailAddress = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                if (emailAddress == null || emailAddress.length() == 0) {
                    error = "Enter your new email address.";
                } else if (emailAddress.equals(oldEmailAddress)) {
                    error = "Your new email address should not be the same as your old email address.";
                }
                if (error != null) {
                    map.put("error", error);
                    finish();
                    return;
                }
                String subject = null;
                String body = null;
                String userId2 = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                if (userId2 != null) {
                    subject = "Notification of attempt to reassign email address";
                    body = "<p>There was an attempt made to change the email of an account " +
                            "from " + oldEmailAddress + " to your email account." +
                            "<p>--Virtual Cow</p>";
                    asyncRequestImpl.send(mailOut.sendEmail(servletContext, emailAddress, subject, body, null),
                            new AsyncResponseProcessor<Boolean>() {
                                @Override
                                public void processAsyncResponse(Boolean _response) throws Exception {
                                    String msg;
                                    if (_response)
                                        msg = "An email has been sent to verify your new address. Please check your inbox.";
                                    else {
                                        msg = "Unable to send an email to your address at this time. Please try again later.";
                                    }
                                    map.put("success", msg);
                                    finish();
                                    return;
                                }
                            });
                } else {
                    long expTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24; // 1 day
                    String token = null;
                    try {
                        token = Tokens.generate(db, userId + emailAddress, expTime);
                    } catch (NoSuchAlgorithmException e) {
                    }
                    if (token == null) {
                        String msg = "Unable to send an email to your address at this time. Please try again later.";
                        map.put("success", msg);
                        finish();
                        return;
                    }
                    String href = SimpleSimon.self +
                            "?to=newEmailAddress&emailAddress=" + emailAddress +
                            "&key=" + token;
                    subject = "Address Validation Request";
                    body = "<p>To validate your new email address, please click " +
                            "<a href=\"" + href + "\">here</a>.</p>" +
                            "<p>--Virtual Cow</p>";
                    asyncRequestImpl.send(mailOut.sendEmail(servletContext, emailAddress, subject, body, null),
                            new AsyncResponseProcessor<Boolean>() {
                                @Override
                                public void processAsyncResponse(Boolean _response) throws Exception {
                                    String msg;
                                    if (_response)
                                        msg = "An email has been sent to verify your new address. Please check your inbox.";
                                    else {
                                        msg = "Unable to send an email to your address at this time. Please try again later.";
                                    }
                                    map.put("success", msg);
                                    finish();
                                    return;
                                }
                            });
                }
            }
        }.signal();
    }
}
