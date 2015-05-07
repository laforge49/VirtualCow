package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.NpjeTransaction;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;

/**
 * Request for home page.
 */
public class NPJEBlade extends RequestBlade {
    public NPJEBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
    }

    public void getNPJE(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }

    public void postNPJE(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String subject = request.getParameter("subject");
                String body = request.getParameter("body");
                map.put("body", SimpleSimon.encode(body, 0, SimpleSimon.ENCODE_FIELD)); //text area
                if (subject.length() > 0)
                    map.put("subject", SimpleSimon.encode(subject, 0, SimpleSimon.ENCODE_FIELD)); //field
                MapNode mn = db.dbFactoryRegistry.nilMap;
                if (subject.length() > 0)
                    mn = mn.add(NameIds.SUBJECT, subject);
                if (body.length() > 0)
                    mn = mn.add(NameIds.BODY, body);
                if (userId != null)
                    mn = mn.add(User.USER_KEY, userId);
                mn = mn.add(NameIds.REMOTE_HOST, request.getRemoteHost());
                mn = mn.add(NameIds.REMOTE_ADDR, request.getRemoteAddr());
                mn = mn.add(NameIds.REMOTE_PORT, request.getRemotePort());

                asyncRequestImpl.send(db.update(NpjeTransaction.NAME, mn), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(String _response) throws Exception {
                        map.put("success", "posted: " + SimpleSimon.niceTime(_response));
                        finish();
                    }
                });
            }
        }.signal();
    }
}
