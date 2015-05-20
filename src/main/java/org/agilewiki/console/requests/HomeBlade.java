package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.console.User;
import org.agilewiki.utils.immutable.FactoryRegistry;

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
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String myEmail = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                map.put("myEmail", myEmail);
                String timestamp = request.getParameter("timestamp");
                if (timestamp == null) {
                    map.put("post", "post");
                } else {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                }
                finish();
            }
        }.signal();
    }
}
