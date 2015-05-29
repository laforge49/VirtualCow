package org.agilewiki.console;

import org.agilewiki.console.user.LogoutTransaction;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request for post requests.
 */
public abstract class PostRequestBlade extends RequestBlade {
    public PostRequestBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(LogoutTransaction.NAME, LogoutTransaction.class);
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        try {
            ((HttpServletResponse) asyncContext.getResponse()).
                    sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
        }
        asyncContext.complete();
    }

    public abstract void post(String page, AsyncContext asyncContext, String userId, Role role);
}
