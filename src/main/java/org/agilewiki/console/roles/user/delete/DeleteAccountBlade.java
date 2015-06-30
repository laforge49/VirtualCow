package org.agilewiki.console.roles.user.delete;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;

/**
 * Request for deleting an account.
 */
public class DeleteAccountBlade extends PostRequestBlade {

    public DeleteAccountBlade(Role role, String page) throws Exception {
        super(role, page);
        Delete_Node.create(ooDb);
        ooDb.registerTransaction(Delete_NodeInstance.NAME, Delete_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "Delete Account";
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
                String oldPassword = request.getParameter("password");
                if (oldPassword == null || oldPassword.length() == 0) {
                    map.put("error", "Enter your password in the old password field");
                    finish();
                    return;
                }
                if (!latest_user_nodeInstance.confirmPassword(servletContext, oldPassword)) {
                    map.put("error", "Incorrect password");
                    finish();
                    return;
                }
                String email = latest_user_nodeInstance.getEmailAddress();
                MapNode mn = ooDb.nilMap;
                mn = mn.add(NameIds.AN_ID, userId);
                mn = mn.add(NameIds.SUBJECT, email);
                asyncRequestImpl.send(ooDb.update(Delete_NodeInstance.NAME, mn),
                        new AsyncResponseProcessor<String>() {
                            @Override
                            public void processAsyncResponse(String _response) throws Exception {
                                response.sendRedirect("?from=deleteAccount");
                                asyncContext.complete();
                                asyncRequestImpl.send(mailOut.sendEmail(servletContext,
                                                email,
                                                "Delete Account Notification",
                                                "<p>Your account has been deleted.</p>" +
                                                        "<p>--Virtual Cow</p>",
                                                null),
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