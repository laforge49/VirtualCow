package org.agilewiki.console.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ContactBlade extends RequestBlade {
    public ContactBlade(SimpleSimon simpleSimon, String page) throws Exception {
        super(simpleSimon, page);
    }

    @Override
    protected String niceName() {
        return "Contact";
    }

    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }
}
