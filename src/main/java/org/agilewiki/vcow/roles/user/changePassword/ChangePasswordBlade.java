package org.agilewiki.vcow.roles.user.changePassword;

import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.Tokens;
import org.agilewiki.vcow.VCUser_Node;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;

/**
 * Requests for changing a password.
 */
public class ChangePasswordBlade extends PostRequestBlade {

    public ChangePasswordBlade(Role role, String page) throws Exception {
        super(role, page);
        ChangePassword_NodeFactory.create(awDb);
        awDb.registerTransaction(ChangePassword_Node.NAME, ChangePassword_Node.class);
    }

    @Override
    public String niceName() {
        return "Change Password";
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
                if (!latest_user_node.confirmPassword(servletContext, oldPassword)) {
                    map.put("error", "Enter your current password in the old password field");
                    finish();
                    return;
                }
                MapNode mn = awDb.nilMap;
                mn = mn.add(NameIds.USER_KEY, userId);
                mn = mn.add(NameIds.PASSWORD_KEY, VCUser_Node.encodePassword(servletContext, userId, newPassword));
                asyncRequestImpl.send(awDb.update(ChangePassword_Node.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                long expTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3; // 3 days
                                String token = null;
                                try {
                                    VCUser_Node user_node = (VCUser_Node) awDb.fetchNode(userId, FactoryRegistry.MAX_TIMESTAMP);
                                    token = Tokens.generate(user_node.passwordDigest(), expTime);
                                } catch (NoSuchAlgorithmException e) {
                                }
                                Cookie loginCookie = new Cookie("userId", userId + "|" + token);
                                loginCookie.setMaxAge(60 * 60 * 24 * 3); //3 days
                                response.addCookie(loginCookie);
                                String email = latest_user_node.getEmailAddress();
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
