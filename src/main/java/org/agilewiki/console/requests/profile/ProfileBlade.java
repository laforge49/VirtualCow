package org.agilewiki.console.requests.profile;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.requests.RequestBlade;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class ProfileBlade extends RequestBlade {
    public ProfileBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "profile";
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