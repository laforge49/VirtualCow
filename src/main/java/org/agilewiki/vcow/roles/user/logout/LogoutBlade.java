package org.agilewiki.vcow.roles.user.logout;

import org.agilewiki.awdb.db.immutable.collections.MapNode;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.PostRequestBlade;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;
import javax.servlet.http.Cookie;

/**
 * Request for logging out.
 */
public class LogoutBlade extends PostRequestBlade {
    public LogoutBlade(Role role, String page) throws Exception {
        super(role, page);
        Logout_NodeFactory.create(awDb);
        awDb.registerTransaction(Logout_Node.NAME, Logout_Node.class);
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
                String email = latest_user_node.getEmailAddress();
                MapNode mn = awDb.nilMap;
                mn = mn.add(NameIds.SUBJECT, email);
                mn = mn.add(NameIds.USER_KEY, userId);
                asyncRequestImpl.send(awDb.update(Logout_Node.NAME, mn), new AsyncResponseProcessor<String>() {
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
