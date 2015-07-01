package org.agilewiki.vcow.roles.developer;

import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;

/**
 * Request for home page.
 */
public class DeveloperBlade extends RequestBlade {
    public DeveloperBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Developer Home";
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
