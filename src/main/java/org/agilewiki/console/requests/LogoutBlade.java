package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.LogoutTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request for logging out.
 */
public class LogoutBlade extends RequestBlade {
    public LogoutBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
        db.registerTransaction(LogoutTransaction.NAME, LogoutTransaction.class);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        try {
            ((HttpServletResponse) asyncContext.getResponse()).
                    sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
        }
        asyncContext.complete();
    }

    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String email = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
                MapNode mn = db.dbFactoryRegistry.nilMap;
                mn = mn.add(NameIds.SUBJECT, email);
                mn = mn.add(User.USER_KEY, userId);
                asyncRequestImpl.send(db.update(LogoutTransaction.NAME, mn), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(String _response) throws Exception {
                        Cookie[] cookies = request.getCookies();
                        if (cookies != null)
                            for (Cookie cookie : cookies) {
                                if (cookie.getName().equals("userId")) {
                                    cookie.setMaxAge(0);
                                    response.addCookie(cookie);
                                    break;
                                }
                            }
                        redirect("?from=logout");
                    }
                });
            }
        }.signal();
    }
}
