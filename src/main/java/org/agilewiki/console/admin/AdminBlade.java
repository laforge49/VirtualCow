package org.agilewiki.console.admin;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class AdminBlade extends RequestBlade {
    public AdminBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    protected String niceName() {
        return "Admin Home";
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
}
