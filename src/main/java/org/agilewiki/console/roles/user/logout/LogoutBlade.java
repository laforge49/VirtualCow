package org.agilewiki.console.roles.user.logout;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;

/**
 * Request for logging out.
 */
public class LogoutBlade extends PostRequestBlade {
    public LogoutBlade(Role role, String page) throws Exception {
        super(role, page);
        Logout_Node.create(awDb);
        awDb.registerTransaction(Logout_NodeInstance.NAME, Logout_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "Log Out";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        post(page, asyncContext, userId, role);
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String email = latest_user_nodeInstance.getEmailAddress();
                MapNode mn = awDb.nilMap;
                mn = mn.add(NameIds.SUBJECT, email);
                mn = mn.add(NameIds.USER_KEY, userId);
                asyncRequestImpl.send(awDb.update(Logout_NodeInstance.NAME, mn), new AsyncResponseProcessor<String>() {
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
