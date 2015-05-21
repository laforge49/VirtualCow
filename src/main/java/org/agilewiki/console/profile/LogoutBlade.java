package org.agilewiki.console.profile;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.requests.PostRequestBlade;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;

/**
 * Request for logging out.
 */
public class LogoutBlade extends PostRequestBlade {
    public LogoutBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
        db.registerTransaction(LogoutTransaction.NAME, LogoutTransaction.class);
    }

    @Override
    protected String groupName() {
        return "profile";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
        post(page, asyncContext, userId);
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
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
