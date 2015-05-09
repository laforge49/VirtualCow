package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.transactions.ChangeEmailAddressTransaction;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ChangeEmailAddressBlade extends PostRequestBlade {
    public ChangeEmailAddressBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(ChangeEmailAddressTransaction.NAME, ChangeEmailAddressTransaction.class);
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
            }
        }.signal();
    }
}
