package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Request for home page.
 */
public class Home extends NonBlockingBladeBase {

    public Home() throws Exception {
    }

    public AReq<Void> getHome(ServletContext servletContext,
                              AsyncContext asyncContext) {
        return new AReq<Void>("getHome") {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            @Override
            protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                                 AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                try {
                    Map<String, String> map = new HashMap<>();
                    String timestamp = request.getParameter("timestamp");
                    String dateInString = request.getParameter("date");
                    if (dateInString != null && dateInString.length() > 0) {
                        Date date;
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                        try {
                            date = formatter.parse(dateInString);
                        } catch (ParseException e) {
                            throw new ServletException(e);
                        }
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        calendar.set(Calendar.SECOND, 59);
                        long time = calendar.getTimeInMillis() + 999;
                        timestamp = TimestampIds.value(TimestampIds.timestampId((time << 10) + 1023));
                    }
                    if (timestamp == null) {
                        map.put("post", "post");
                        response.getWriter().println(SimpleSimon.replace(servletContext, "home", map));
                        response.setStatus(HttpServletResponse.SC_OK);
                        asyncContext.complete();
                        _asyncResponseProcessor.processAsyncResponse(null);
                        return;
                    }
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("setStartingAt", "&startingAt=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    map.put("clearTime", "<a href=\".\">Clear selected time</a>");
                    response.getWriter().println(SimpleSimon.replace(servletContext, "home", map));
                    response.setStatus(HttpServletResponse.SC_OK);
                    asyncContext.complete();
                    _asyncResponseProcessor.processAsyncResponse(null);
                } catch (Exception ex) {
                    servletContext.log("home", ex);
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
