package org.agilewiki.console.oodb.nodes.roles.unRole.login;

import org.agilewiki.console.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class LoginBlade extends PostRequestBlade {
    public LoginBlade(Role role, String page) throws Exception {
        super(role, page);
        Login_Node.create();
        db.registerTransaction(Login_NodeInstance.NAME, Login_NodeInstance.class);
        BadUserAddress_Node.create();
        db.registerTransaction(BadUserAddress_NodeInstance.NAME, BadUserAddress_NodeInstance.class);
        BadUserPassword_Node.create();
        db.registerTransaction(BadUserPassword_NodeInstance.NAME, BadUserPassword_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "Login";
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
                String password = request.getParameter("password");
                String error = null;
                if (emailAddress != null)
                    map.put("emailAddress", SimpleSimon.encode(emailAddress, 0, SimpleSimon.ENCODE_FIELD)); //field
                if (emailAddress == null)
                    error = "Email address is required";
                else if (password == null || password.length() == 0)
                    error = "Password is required";
                if (error != null) {
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String userId = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                if (userId == null) {
                    MapNode mn = db.dbFactoryRegistry.nilMap;
                    mn = mn.add(NameIds.SUBJECT, emailAddress);
                    mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                    mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                    mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
                    asyncRequestImpl.send(db.update(BadUserAddress_NodeInstance.NAME, mn),
                            new AsyncResponseProcessor<String>() {
                                @Override
                                public void processAsyncResponse(String _response) throws Exception {
                                    map.put("error", SimpleSimon.encode("Invalid email address / password",
                                            0, SimpleSimon.ENCODE_FIELD)); //field
                                    finish();
                                }
                            });
                    return;
                }
                if (!User.confirmPassword(db, servletContext, userId, password)) {
                    MapNode mn = db.dbFactoryRegistry.nilMap;
                    mn = mn.add(NameIds.SUBJECT, emailAddress);
                    mn = mn.add(User.USER_KEY, userId);
                    mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                    mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                    mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
                    asyncRequestImpl.send(db.update(BadUserPassword_NodeInstance.NAME, mn),
                            new AsyncResponseProcessor<String>() {
                                @Override
                                public void processAsyncResponse(String _response) throws Exception {
                                    map.put("error", SimpleSimon.encode("Invalid email address / password",
                                            0, SimpleSimon.ENCODE_FIELD)); //field
                                    finish();
                                }
                            });
                    return;
                }
                long expTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3; // 3 days
                String token = null;
                try {
                    token = Tokens.generate(db, User.passwordDigest(db, userId, FactoryRegistry.MAX_TIMESTAMP), expTime);
                } catch (NoSuchAlgorithmException e) {
                    servletContext.log("no such algorithm: SHA-256");
                    map.put("error", SimpleSimon.encode("Unable to create your account at this time. Please try again later.",
                            0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                Cookie loginCookie = new Cookie("userId", userId + "|" + token);
                loginCookie.setMaxAge(60 * 60 * 24 * 3); //3 days
                response.addCookie(loginCookie);
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(NameIds.SUBJECT, emailAddress);
                mn = mn.add(User.USER_KEY, userId);
                mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());
                asyncRequestImpl.send(db.update(Login_NodeInstance.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                redirect("?from=login");
                            }
                        });
            }
        }.signal();
    }
}