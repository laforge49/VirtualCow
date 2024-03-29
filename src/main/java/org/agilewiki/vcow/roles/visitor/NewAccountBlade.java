package org.agilewiki.vcow.roles.visitor;

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
public class NewAccountBlade extends PostRequestBlade {
    public NewAccountBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "New Account";
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
                String userId = VCUser_Node.userId(emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String subject = null;
                String body = null;
                if (userId == null) {
                    try {
                        String token = Tokens.generate(emailAddress,
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
                AReq<Boolean> mout = mailOut.sendEmail(servletContext, emailAddress, subject, body, null);
                asyncRequestImpl.send(mout,
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
