package org.agilewiki.console.oodb.nodes.roles.unRole;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class WelcomeBlade extends RequestBlade {
    public WelcomeBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    protected String niceName() {
        return "Welcome";
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
