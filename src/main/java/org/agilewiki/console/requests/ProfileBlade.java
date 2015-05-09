package org.agilewiki.console.requests;

import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;

/**
 * Change the email address of the user.
 */
public class ProfileBlade extends RequestBlade {
    public ProfileBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
    }

    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }
}
