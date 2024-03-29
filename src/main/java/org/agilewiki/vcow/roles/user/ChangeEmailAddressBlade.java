package org.agilewiki.vcow.roles.user;

import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.Tokens;
import org.agilewiki.vcow.VCUser_Node;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class ChangeEmailAddressBlade extends PostRequestBlade {
    public ChangeEmailAddressBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Change Email Address";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String emailAddress = request.getParameter("emailAddress");
                if (emailAddress != null)
                    map.put("emailAddress", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                String error = null;
                String oldEmailAddress = latest_user_node.getEmailAddress();
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
                String userId2 = VCUser_Node.userId(emailAddress, FactoryRegistry.MAX_TIMESTAMP);
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
                        token = Tokens.generate(userId + emailAddress, expTime);
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
