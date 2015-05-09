package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.transactions.NewUserTransaction;

import javax.servlet.AsyncContext;

/**
 * Create a new user with a validated email address.
 */
public class ValidatedBlade extends PostRequestBlade {
    public ValidatedBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(NewUserTransaction.NAME, NewUserTransaction.class);
    }

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
