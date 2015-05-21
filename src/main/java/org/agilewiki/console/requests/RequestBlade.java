package org.agilewiki.console.requests;

import org.agilewiki.console.MailOut;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.console.User;
import org.agilewiki.jactor2.core.blades.NonBlockingBladeBase;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.ExceptionHandler;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        protected final String page;
        protected final String userId;
        protected String myEmail = null;
        protected AsyncRequestImpl asyncRequestImpl;
        protected AsyncResponseProcessor<Void> asyncResponseProcessor;
        protected Map<String, String> map;
        protected String timestamp;
        protected String dateInString;
        protected long longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        protected String setContext;

        public SR(String page, AsyncContext asyncContext, String userId) {
            super(page);
            this.asyncContext = asyncContext;
            request = (HttpServletRequest) asyncContext.getRequest();
            response = (HttpServletResponse) asyncContext.getResponse();
            this.page = page;
            this.userId = userId;
            if (userId != null) {
                myEmail = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
            }
        }

        abstract protected void process()
                throws Exception;

        protected String setContext() {
            return "";
        }

        protected void processAsyncOperation(AsyncRequestImpl _asyncRequestImpl,
                                             AsyncResponseProcessor<Void> _asyncResponseProcessor)
                throws Exception {
            asyncRequestImpl = _asyncRequestImpl;
            asyncResponseProcessor = _asyncResponseProcessor;
            map = new HashMap<>();
            setContext = setContext();
            if (userId != null) {
                map.put("myEmail", myEmail);
                map.put("guest", "<a>-guest-</a>");
            }
            timestamp = request.getParameter("timestamp");
            dateInString = request.getParameter("date");
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
            if (timestamp != null) {
                map.put("clearTime", "<a href=\"?from=" + page + "&to=" + page + setContext + "\">Clear selected time</a>");
                map.put("setTimestamp", "&timestamp=" + timestamp + setContext);
                map.put("hiddenTimestamp",
                        "<input type=\"hidden\" name=\"timestamp\" value=\"" + timestamp + "\" />");
                String timestampId = TimestampIds.generate(timestamp);
                map.put("atTime", "at " + SimpleSimon.niceTime(timestampId));
                longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
            }
            if ("home".equals(page)) {
                map.put("home", "<a>home</a>");
            } else if (timestamp == null) {
                map.put("home", "<a href=\"?from=" + page + "&to=home#rupa\">-home-</a>");
            } else {
                map.put("home", "<a href=\"?from=" + page + "&to=home&timestamp=" + timestamp + "#rupa\">-home-</a>");
            }
            _asyncRequestImpl.setExceptionHandler(new ExceptionHandler() {
                @Override
                public Object processException(Exception e) throws Exception {
                    servletContext.log(page, e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    asyncContext.complete();
                    _asyncResponseProcessor.processAsyncResponse(null);
                    return null;
                }
            });
            process();
        }

        protected void finish() throws Exception {
            response.getWriter().println(SimpleSimon.replace(servletContext, page, map));
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
