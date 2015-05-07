package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.console.transactions.NpjeTransaction;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.immutable.collections.MapNode;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Request for home page.
 */
public class NPJE extends NonBlockingBladeBase {
    ServletContext servletContext;
    Db db;

    public NPJE(ServletContext servletContext, Db db) throws Exception {
        this.servletContext = servletContext;
        this.db = db;
    }

    public ASig getNPJE(AsyncContext asyncContext) {
        return new ASig("getNPJE") {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public Object processException(Exception e) throws Exception {
                        servletContext.log("getNPJE", e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                        return null;
                    }
                });
                Map<String, String> map = new HashMap<>();
                response.getWriter().println(SimpleSimon.replace(servletContext, "post", map));
                response.setStatus(HttpServletResponse.SC_OK);
                asyncContext.complete();
                _asyncResponseProcessor.processAsyncResponse(null);
            }
        };
    }

    public ASig postNPJE(AsyncContext asyncContext, String userId) {
        return new ASig("postNPJE") {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public Object processException(Exception e) throws Exception {
                        servletContext.log("postNPJE", e);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                        return null;
                    }
                });
                Map<String, String> map = new HashMap<>();

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

                _asyncRequestImpl.send(db.update(NpjeTransaction.NAME, mn), new AsyncResponseProcessor<String>() {
                    @Override
                    public void processAsyncResponse(String _response) throws Exception {
                        map.put("success", "posted: " + SimpleSimon.niceTime(_response));
                        response.getWriter().println(SimpleSimon.replace(servletContext, "post", map));
                        response.setStatus(HttpServletResponse.SC_OK);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                    }
                });
            }
        };
    }
}
