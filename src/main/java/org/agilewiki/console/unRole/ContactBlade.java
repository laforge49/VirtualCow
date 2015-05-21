package org.agilewiki.console.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ContactBlade extends RequestBlade {
    public ContactBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }
}
