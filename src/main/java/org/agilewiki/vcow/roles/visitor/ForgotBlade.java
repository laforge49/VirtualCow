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
public class ForgotBlade extends PostRequestBlade {
    public ForgotBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Forgot";
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
                String emailAddress = request.getParameter("emailAddress3");
                if (emailAddress != null)
                    map.put("emailAddress3", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                if (emailAddress == null || emailAddress.length() == 0)
                    error = "Email address is required";
                if (error != null) {
                    map.put("error3", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String userId = VCUser_Node.userId(emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String msg = "An email has been sent to verify your address. Please check your inbox.";
                if (userId == null) {
                    map.put("success3", SimpleSimon.encode(msg, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String subject = null;
                String body = null;
                try {
                    String token = Tokens.generate(emailAddress,
                            1000 * 60 * 60 * 24 + System.currentTimeMillis()); //1 day validity
                    subject = "Forgot Password";
                    body = "<p>To change your password, please click " +
                            "<a href=\"" + SimpleSimon.self + "?to=forgotPassword&email=" + emailAddress +
                            "&key=" + token + "\">here</a>.</p>" +
                            "<p>--Virtual Cow</p>";
                } catch (NoSuchAlgorithmException e) {
                    servletContext.log("no such algorithm: SHA-256");
                    msg = "Unable to send an email to your address at this time. Please try again later.";
                    map.put("success3", SimpleSimon.encode(msg, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                asyncRequestImpl.send(mailOut.sendEmail(servletContext, emailAddress, subject, body, null),
                        new AsyncResponseProcessor<Boolean>() {
                            @Override
                            public void processAsyncResponse(Boolean _response) throws Exception {
                                String msg;
                                if (_response)
                                    msg = "An email has been sent to verify your address. Please check your inbox.";
                                else
                                    msg = "Unable to send an email to your address at this time. Please try again later.";
                                map.put("success3", SimpleSimon.encode(msg, 0, SimpleSimon.ENCODE_FIELD)); //field
                                finish();
                            }
                        });
            }
        }.signal();
    }
}
