package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.transactions.LoginTransaction;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class LoginBlade extends RequestBlade {
    public LoginBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(LoginTransaction.NAME, LoginTransaction.class);
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
}
