package org.agilewiki.vcow.roles.visitor;

import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ContactBlade extends RequestBlade {
    public ContactBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
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
