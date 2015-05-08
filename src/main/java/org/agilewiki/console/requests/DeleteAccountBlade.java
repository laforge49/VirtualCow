package org.agilewiki.console.requests;

import org.agilewiki.console.MailOut;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.DeleteTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import javax.mail.MessagingException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;

/**
 * Request for deleting an account.
 */
public class DeleteAccountBlade extends RequestBlade {
    final MailOut mailOut;

    public DeleteAccountBlade(ServletContext servletContext, Db db, MailOut mailOut) throws Exception {
        super(servletContext, db);
        this.mailOut = mailOut;
    }

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
                                                    redirect("?to=login&from=deleteAccount");
                                                }
                                            });
                            }
                        });
            }
        }.signal();
    }
}
