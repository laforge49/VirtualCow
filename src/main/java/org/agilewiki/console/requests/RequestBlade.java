package org.agilewiki.console.requests;

import org.agilewiki.console.MailOut;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.User;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Base page for requests.
 */
public abstract class RequestBlade extends NonBlockingBladeBase {
    protected final ServletContext servletContext;
    protected final Db db;
    protected final MailOut mailOut;

    public RequestBlade(SimpleSimon simpleSimon)
            throws Exception {
        this.servletContext = simpleSimon.servletContext;
        this.db = simpleSimon.db;
        this.mailOut = simpleSimon.mailOut;
    }

    public abstract void get(String page, AsyncContext asyncContext, String userId);

    protected abstract class SR extends ASig {
        protected final AsyncContext asyncContext;
        protected final HttpServletRequest request;
        protected final HttpServletResponse response;
        protected final String opName;
        protected final String userId;
        protected String myEmail = null;
        protected AsyncRequestImpl asyncRequestImpl;
        protected AsyncResponseProcessor<Void> asyncResponseProcessor;
        protected Map<String, String> map;

        public SR(String _opName, AsyncContext asyncContext, String userId) {
            super(_opName);
            this.asyncContext = asyncContext;
            request = (HttpServletRequest) asyncContext.getRequest();
            response = (HttpServletResponse) asyncContext.getResponse();
            opName = _opName;
            this.userId = userId;
            if (userId != null) {
                myEmail = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
            }
        }

        abstract protected void process()
                throws Exception;

        protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                             AsyncResponseProcessor<Void> _asyncResponseProcessor)
                throws Exception {
            asyncRequestImpl = _asyncRequestImpl;
            asyncResponseProcessor = _asyncResponseProcessor;
            map = new HashMap<>();
            if (userId != null) {
                map.put("myEmail", myEmail);
            }
            _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                @Override
                public Object processException(Exception e) throws Exception {
                    servletContext.log(opName, e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    asyncContext.complete();
                    _asyncResponseProcessor.processAsyncResponse(null);
                    return null;
                }
            });
            process();
        }

        protected void finish() throws Exception {
            response.getWriter().println(SimpleSimon.replace(servletContext, opName, map));
            response.setStatus(HttpServletResponse.SC_OK);
            asyncContext.complete();
            asyncResponseProcessor.processAsyncResponse(null);
        }

        protected void redirect(String url) throws Exception {
            response.sendRedirect(url);
            asyncContext.complete();
            asyncResponseProcessor.processAsyncResponse(null);
        }
    }
}
