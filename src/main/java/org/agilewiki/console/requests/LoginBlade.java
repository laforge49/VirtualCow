package org.agilewiki.console.requests;

import org.agilewiki.console.transactions.LoginTransaction;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;

/**
 * Change the email address of the user.
 */
public class LoginBlade extends RequestBlade {
    public LoginBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
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
