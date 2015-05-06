package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Request for home page.
 */
public class NPJE extends NonBlockingBladeBase {
    ServletContext servletContext;

    public NPJE(ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
    }

    public ASig getNPJE(AsyncContext asyncContext) {
        return new ASig("getNPJE") {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                try {
                    Map<String, String> map = new HashMap<>();
                    response.getWriter().println(SimpleSimon.replace(servletContext, "post", map));
                    response.setStatus(HttpServletResponse.SC_OK);
                    asyncContext.complete();
                    _asyncResponseProcessor.processAsyncResponse(null);
                } catch (Exception ex) {
                    servletContext.log("getNPJE", ex);
                    try {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        asyncContext.complete();
                    } catch (Exception e) {
                    }
                    throw ex;
                }
            }
        };
    }
}
