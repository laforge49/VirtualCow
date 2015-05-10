package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.Tokens;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.BadUserAddressTransaction;
import org.agilewiki.console.transactions.BadUserPasswordTransaction;
import org.agilewiki.console.transactions.LoginTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

/**
 * Change the email address of the user.
 */
public class LoginBlade extends PostRequestBlade {
    public LoginBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(LoginTransaction.NAME, LoginTransaction.class);
        db.registerTransaction(BadUserAddressTransaction.NAME, BadUserAddressTransaction.class);
        db.registerTransaction(BadUserPasswordTransaction.NAME, BadUserPasswordTransaction.class);
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

    @Override
    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String login = request.getParameter("login");
                if (login != null && login.equals("Login")) {
                    login();
                    return;
                }
                String newAccount = request.getParameter("newAccount");
                if (newAccount != null && newAccount.equals("Validate Address")) {
                    newAccount();
                    return;
                }
                String forgotPassword = request.getParameter("forgotPassword");
                if (forgotPassword != null && forgotPassword.equals("Verify Address")) {
                    forgotPassword();
                    return;
                }
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                asyncContext.complete();
                asyncResponseProcessor.processAsyncResponse(null);
            }

            void login() throws Exception {
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
                    asyncRequestImpl.send(db.update(BadUserAddressTransaction.NAME, mn),
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
                    asyncRequestImpl.send(db.update(BadUserPasswordTransaction.NAME, mn),
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
                asyncRequestImpl.send(db.update(LoginTransaction.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                redirect("?from=login");
                            }
                        });
            }

            void newAccount() throws Exception {
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

            void forgotPassword() throws Exception {
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
                String userId = User.userId(db, emailAddress, FactoryRegistry.MAX_TIMESTAMP);
                String msg = "An email has been sent to verify your address. Please check your inbox.";
                if (userId == null) {
                    map.put("success3", SimpleSimon.encode(msg, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String subject = null;
                String body = null;
                try {
                    String token = Tokens.generate(db, emailAddress,
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
