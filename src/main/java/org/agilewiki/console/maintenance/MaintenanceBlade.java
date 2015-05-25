package org.agilewiki.console.maintenance;

import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;

import javax.servlet.AsyncContext;

/**
 * Request for home page.
 */
public class MaintenanceBlade extends RequestBlade {
    public MaintenanceBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "maintenance";
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
