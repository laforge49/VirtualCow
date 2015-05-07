package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
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
public class RequestBlade extends NonBlockingBladeBase {
    ServletContext servletContext;
    Db db;

    public RequestBlade(ServletContext servletContext, Db db) throws Exception {
        this.servletContext = servletContext;
        this.db = db;
    }

    protected abstract class SR extends ASig {
        protected final AsyncContext asyncContext;
        protected final HttpServletRequest request;
        protected final HttpServletResponse response;
        protected final String opName;
        protected AsyncRequestImpl asyncRequestImpl;
        protected AsyncResponseProcessor<Void> asyncResponseProcessor;
        protected Map<String, String> map;

        public SR(String _opName, AsyncContext asyncContext) {
            super(_opName);
            this.asyncContext = asyncContext;
            request = (HttpServletRequest) asyncContext.getRequest();
            response = (HttpServletResponse) asyncContext.getResponse();
            opName = _opName;
        }

        abstract protected void process()
                throws Exception;

        protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                             AsyncResponseProcessor<Void> _asyncResponseProcessor)
                throws Exception {
            asyncRequestImpl = _asyncRequestImpl;
            asyncResponseProcessor = _asyncResponseProcessor;
            map = new HashMap<>();
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
    }
}
