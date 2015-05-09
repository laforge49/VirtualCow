package org.agilewiki.console.requests;

import org.agilewiki.console.MailOut;
import org.agilewiki.console.Tokens;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.ChangePasswordTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;

/**
 * Requests for changing a password.
 */
public class ChangePasswordBlade extends RequestBlade {
    final MailOut mailOut;

    public ChangePasswordBlade(ServletContext servletContext, Db db, MailOut mailOut) throws Exception {
        super(servletContext, db);
        this.mailOut = mailOut;
        db.registerTransaction(ChangePasswordTransaction.NAME, ChangePasswordTransaction.class);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String oldPassword = request.getParameter("old");
                String newPassword = request.getParameter("new");
                String confirmNewPassword = request.getParameter("confirm");
                if (oldPassword == null || oldPassword.length() == 0) {
                    map.put("error", "Enter your password in the old password field");
                    finish();
                    return;
                }
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
                if (oldPassword.equals(newPassword)) {
                    map.put("error", "The old password and new password fields must not be the same");
                    finish();
                    return;
                }
                if (!User.confirmPassword(db, servletContext, userId, oldPassword)) {
                    map.put("error", "Enter your current password in the old password field");
                    finish();
                    return;
                }
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(User.USER_KEY, userId);
                mn = mn.add(User.PASSWORD_KEY, User.encodePassword(servletContext, userId, newPassword));
                asyncRequestImpl.send(db.update(ChangePasswordTransaction.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                long expTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3; // 3 days
                                String token = null;
                                try {
                                    token = Tokens.generate(db, User.passwordDigest(db, userId, FactoryRegistry.MAX_TIMESTAMP), expTime);
                                } catch (NoSuchAlgorithmException e) {
                                }
                                Cookie loginCookie = new Cookie("userId", userId + "|" + token);
                                loginCookie.setMaxAge(60 * 60 * 24 * 3); //3 days
                                response.addCookie(loginCookie);
                                String email = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                                asyncRequestImpl.send(mailOut.sendEmail(servletContext,
                                                email,
                                                "Password Change Notification",
                                                "<p>Your password has been changed.</p>" +
                                                        "<p>--Virtual Cow</p>",
                                                null),
                                        new AsyncResponseProcessor<Boolean>() {
                                            @Override
                                            public void processAsyncResponse(Boolean _response) throws Exception {
                                                map.put("success", "The password has been changed.");
                                                finish();
                                            }
                                        });
                            }
                        });
            }
        }.signal();
    }
}
