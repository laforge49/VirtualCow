package org.agilewiki.console.roles.developer.npje;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.PostRequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.roles.Role;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.collections.MapNode;

import javax.servlet.AsyncContext;

/**
 * Request for a non-performing journal entry.
 */
public class PostBlade extends PostRequestBlade {
    public PostBlade(Role role, String page) throws Exception {
        super(role, page);
        Npje_Node.create(awDb);
        awDb.registerTransaction(Npje_NodeInstance.NAME, Npje_NodeInstance.class);
    }

    @Override
    public String niceName() {
        return "Post";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    @Override
    public void post(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String subject = request.getParameter("subject");
                String body = request.getParameter("body");
                map.put("body", SimpleSimon.encode(body, 0, SimpleSimon.ENCODE_FIELD)); //text area
                if (subject.length() > 0)
                    map.put("subject", SimpleSimon.encode(subject, 0, SimpleSimon.ENCODE_FIELD)); //field
                MapNode mn = awDb.nilMap;
                if (subject.length() > 0)
                    mn = mn.add(NameIds.SUBJECT, subject);
                if (body.length() > 0)
                    mn = mn.add(NameIds.BODY, body);
                if (userId != null)
                    mn = mn.add(NameIds.USER_KEY, userId);
                mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());

                asyncRequestImpl.send(awDb.update(Npje_NodeInstance.NAME, mn), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(String _response) throws Exception {
                        map.put("subject", "");
                        map.put("body", "");
                        map.put("success", "posted: " + SimpleSimon.niceTime(_response));
                        finish();
                    }
                });
            }
        }.signal();
    }
}
