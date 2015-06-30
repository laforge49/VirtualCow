package org.agilewiki.console.roles.visitor.newUser;

import org.agilewiki.console.*;
import org.agilewiki.awdb.AwDb;
import org.agilewiki.console.roles.Role;
import org.agilewiki.console.roles.Role_NodeInstance;
import org.agilewiki.console.roles.developer.Developer_Role;
import org.agilewiki.console.roles.user.User_Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import java.security.NoSuchAlgorithmException;

/**
 * Create a new user with a validated email address.
 */
public class ValidatedBlade extends PostRequestBlade {
    public ValidatedBlade(Role role, String page) throws Exception {
        super(role, page);
        NewUser_NodeFactory.create(awDb);
        awDb.registerTransaction(NewUser_Node.NAME, NewUser_Node.class);
    }

    @Override
    public String niceName() {
        return "Validated";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                map.put("submit", "<input type=\"image\" src=\"images/submitbutton.jpg\" name=\"submit\" value=\"submit\"/>");
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
                map.put("key", key);
                String email = request.getParameter("email");
                map.put("email", email);
                String password = request.getParameter("new");
                String confirm = request.getParameter("confirm");
                String error = null;
                if (password == null || password.length() == 0)
                    error = "Enter your new password in the new password field";
                else if (!password.equals(confirm))
                    error = "The new password and confirm new password fields must be the same";
                if (error != null) {
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    map.put("submit", "<input type=\"image\" src=\"images/submitbutton.jpg\" name=\"submit\" value=\"submit\"/>");
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
                    error = "Unable to create your account at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    map.put("submit", "<input type=\"image\" src=\"images/submitbutton.jpg\" name=\"submit\" value=\"submit\"/>");
                    finish();
                    return;
                }
                String userId = AwDb.randomId.generate();
                String passwordHash = User_NodeInstance.encodePassword(servletContext, userId, password);
                if (passwordHash == null) {
                    error = "Unable to create your account at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    map.put("submit", "<input type=\"image\" src=\"images/submitbutton.jpg\" name=\"submit\" value=\"submit\"/>");
                    finish();
                    return;
                }
                MapNode mn = awDb.nilMap;
                mn = mn.add(NameIds.USER_KEY, userId);
                String emailId = ValueId.generate(email);
                mn = mn.add(NameIds.EMAIL_ID, emailId);
                mn = mn.add(NameIds.PASSWORD_KEY, passwordHash);
                mn = mn.add(NameIds.ROLE_ID, Role_NodeInstance.roleName(User_Role.ID));
                mn = mn.add(NameIds.ROLE_ID, Role_NodeInstance.roleName(Developer_Role.ID));
                asyncRequestImpl.send(awDb.update(NewUser_Node.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                map.put("submit", "");
                                map.put("success", "<table><tr><td><p>Your account has been created and you can now</p></td><td>" +
                                        "<a href=\"?from=validated&to=login#rupa\"><img src=\"images/login2button.jpg\" /></a>." +
                                        "</td></tr></table>");
                                finish();
                            }
                        });
            }
        }.signal();
    }
}
