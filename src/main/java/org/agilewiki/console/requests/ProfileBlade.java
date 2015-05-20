package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.utils.immutable.FactoryRegistry;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ProfileBlade extends RequestBlade {
    public ProfileBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
            @Override
            protected void process()
                    throws Exception {
                String myEmail = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                map.put("myEmail", myEmail);
                finish();
            }
        }.signal();
    }
}
