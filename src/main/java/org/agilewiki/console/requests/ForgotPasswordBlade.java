package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.NpjeTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ForgotPasswordBlade extends PostRequestBlade {
    public ForgotPasswordBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
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
    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
            }
        }.signal();
    }
}
