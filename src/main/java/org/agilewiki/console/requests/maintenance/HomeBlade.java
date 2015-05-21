package org.agilewiki.console.requests.maintenance;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.requests.RequestBlade;

import javax.servlet.AsyncContext;

/**
 * Request for home page.
 */
public class HomeBlade extends RequestBlade {
    public HomeBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                if (timestamp == null) {
                    map.put("post", "post");
                }
                finish();
            }
        }.signal();
    }
}
