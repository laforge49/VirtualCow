package org.agilewiki.console.unRole;

import org.agilewiki.console.*;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import java.security.NoSuchAlgorithmException;

/**
 * Create a new user with a validated email address.
 */
public class ValidatedBlade extends PostRequestBlade {
    public ValidatedBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(NewUserTransaction.NAME, NewUserTransaction.class);
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
                    finish();
                    return;
                }
                boolean go = true;
                try {
                    go = Tokens.validate(db, email, key);
                } catch (NoSuchAlgorithmException e) {
                    servletContext.log("no such algorithm: SHA-256");
                    go = false;
                }
                if (!go) {
                    error = "Unable to create your account at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                String userId = RandomIds.randomId.generate();
                String passwordHash = User.encodePassword(servletContext, userId, password);
                if (passwordHash == null) {
                    error = "Unable to create your account at this time. Please try again later.";
                    map.put("error", SimpleSimon.encode(error, 0, SimpleSimon.ENCODE_FIELD)); //field
                    finish();
                    return;
                }
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(User.USER_KEY, userId);
                String emailId = ValueId.generate(email);
                mn = mn.add(User.EMAIL_ID, emailId);
                mn = mn.add(User.PASSWORD_KEY, passwordHash);
                mn = mn.add(User.USER_TYPE_ID,
                        simpleSimon.profileRole.roleName());
                mn = mn.add(User.USER_TYPE_ID,
                        simpleSimon.maintenanceRole.roleName());
                asyncRequestImpl.send(db.update(NewUserTransaction.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                map.put("success", "Your account has been created and you can now " +
                                        "<a href=\"?from=validated&to=login#rupa\">login</a>.");
                                finish();
                            }
                        });
            }
        }.signal();
    }
}
