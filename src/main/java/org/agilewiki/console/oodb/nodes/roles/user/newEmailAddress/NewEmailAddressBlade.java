package org.agilewiki.console.oodb.nodes.roles.user.newEmailAddress;

import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.Tokens;
import org.agilewiki.console.User;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class NewEmailAddressBlade extends PostRequestBlade {
    public NewEmailAddressBlade(Role role, String page) throws Exception {
        super(role, page);
        NewEmailAddress_Node.create();
        ooDb.registerTransaction(NewEmailAddress_NodeInstance.NAME, NewEmailAddress_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "New Email Address";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String key = request.getParameter("key");
                String email = request.getParameter("emailAddress");
                map.put("key", key);
                map.put("emailAddress", email);
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
                String token = request.getParameter("key");
                String password = request.getParameter("password");
                map.put("emailAddress", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                map.put("key", SimpleSimon.encode(token, 0, SimpleSimon.ENCODE_FIELD)); //field
                if (password == null || password.length() == 0) {
                    String error = "Password is required";
                    map.put("error", error);
                    finish();
                    return;
                }
                if (!User.confirmPassword(servletContext, userId, password)) {
                    map.put("error", "Password does not match.");
                    finish();
                    return;
                }
                Boolean go = false;
                try {
                    go = Tokens.validate(userId + emailAddress, token);
                } catch (NoSuchAlgorithmException e) {
                }
                if (!go) {
                    map.put("success", "Unable to update your account at this time. Please try again later.");
                    finish();
                    return;
                }
                String oldEmailAddress = User.email(userId, FactoryRegistry.MAX_TIMESTAMP);
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(User.USER_KEY, userId);
                mn = mn.add(User.EMAIL_ID, emailAddress);
                asyncRequestImpl.send(ooDb.update(NewEmailAddress_NodeInstance.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                myEmail = User.email(userId, FactoryRegistry.MAX_TIMESTAMP);
                                map.put("myEmail", myEmail);
                                map.put("success", "The email address for your account has been updated.");
                                println();
                                response.setStatus(HttpServletResponse.SC_OK);
                                asyncContext.complete();
                                String subject = "Address change notification";
                                String body = "<p>Your account is now tied to your new email address: " +
                                        emailAddress + "</p>" +
                                        "<p>--Virtual Cow</p>";
                                asyncRequestImpl.send(mailOut.sendEmail(servletContext, oldEmailAddress, subject, body, null),
                                        new AsyncResponseProcessor<Boolean>() {
                                            @Override
                                            public void processAsyncResponse(Boolean _response) throws Exception {
                                                asyncResponseProcessor.processAsyncResponse(null);
                                            }
                                        });
                            }
                        });
            }
        }.signal();
    }
}
