package org.agilewiki.console.roles.visitor.forgotPassword;

import org.agilewiki.console.*;
import org.agilewiki.console.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class ForgotPasswordBlade extends PostRequestBlade {
    public ForgotPasswordBlade(Role role, String page) throws Exception {
        super(role, page);
        ForgotPassword_Node.create(awDb);
        awDb.registerTransaction(ForgotPassword_NodeInstance.NAME, ForgotPassword_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "Forgot Password";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String key = request.getParameter("key");
                String email = request.getParameter("email");
                map.put("key", key);
                map.put("email", email);
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
                String key = request.getParameter("key");
                String email = request.getParameter("email");
                map.put("key", key);
                map.put("email", email);
                String newPassword = request.getParameter("new");
                String confirmNewPassword = request.getParameter("confirm");
                if (newPassword == null || newPassword.length() == 0) {
                    map.put("error", "Enter your new password in the new password field");
                    finish();
                    return;
                }
                if (!newPassword.equals(confirmNewPassword)) {
                    map.put("error", "The new password and confirm new password fields must be the same");
                    finish();
                    return;
                }
                boolean go = true;
                try {
                    go = Tokens.validate(email, key);
                } catch (NoSuchAlgorithmException e) {
                    servletContext.log("no such algorithm: SHA-256");
                    go = false;
                }
                if (!go) {
                    String error = "Unable to change your password at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String userId = User_NodeInstance.userId(email, FactoryRegistry.MAX_TIMESTAMP);
                if (userId == null) {
                    String error = "Unable to change your password at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                MapNode mn = awDb.nilMap;
                mn = mn.add(NameIds.USER_KEY, userId);
                mn = mn.add(NameIds.PASSWORD_KEY, User_NodeInstance.encodePassword(servletContext, userId, newPassword));
                asyncRequestImpl.send(awDb.update(ForgotPassword_NodeInstance.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                map.put("success", "The password has been changed and you can now " +
                                        "<a href=\"?from=validated&to=login#rupa\">login</a>.");
                                println();
                                response.setStatus(HttpServletResponse.SC_OK);
                                asyncContext.complete();
                                String subject = "Password Change Notification";
                                String body = "<p>Your password has been changed.</p>" +
                                        "<p>--Virtual Cow</p>";
                                asyncRequestImpl.send(mailOut.sendEmail(servletContext, email, subject, body, null),
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
