package org.agilewiki.console.profile;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;

/**
 * Request for deleting an account.
 */
public class DeleteAccountBlade extends PostRequestBlade {

    public DeleteAccountBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(DeleteTransaction.NAME, DeleteTransaction.class);
    }

    @Override
    protected String groupName() {
        return "profile";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                String oldPassword = request.getParameter("password");
                if (oldPassword == null || oldPassword.length() == 0) {
                    map.put("error", "Enter your password in the old password field");
                    finish();
                    return;
                }
                if (!User.confirmPassword(db, servletContext, userId, oldPassword)) {
                    map.put("error", "Incorrect password");
                    finish();
                    return;
                }
                String email = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(NameIds.AN_ID, userId);
                mn = mn.add(NameIds.SUBJECT, email);
                asyncRequestImpl.send(db.update(DeleteTransaction.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                asyncRequestImpl.send(mailOut.sendEmail(servletContext,
                                                email,
                                                "Delete Account Notification",
                                                "<p>Your account has been deleted.</p>" +
                                                        "<p>--Virtual Cow</p>",
                                                null),
                                        new AsyncResponseProcessor<Boolean>() {
                                            @Override
                                            public void processAsyncResponse(Boolean _response) throws Exception {
                                                redirect("?from=deleteAccount");
                                            }
                                        });
                            }
                        });
            }
        }.signal();
    }
}
