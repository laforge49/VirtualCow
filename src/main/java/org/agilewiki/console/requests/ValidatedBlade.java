package org.agilewiki.console.requests;

import org.agilewiki.console.transactions.NewUserTransaction;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;

/**
 * Create a new user with a validated email address.
 */
public class ValidatedBlade extends RequestBlade {
    public ValidatedBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
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
}
