package org.agilewiki.console;

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

    protected String groupName() {
        return null;
    }

    public abstract void get(String page, AsyncContext asyncContext, String userId, Role role);

    protected abstract class SR extends ASig {
        protected final AsyncContext asyncContext;
        protected final HttpServletRequest request;
        protected final HttpServletResponse response;
        protected final String page;
        protected final String userId;
        protected final Role role;
        protected String myEmail = null;
        protected AsyncRequestImpl asyncRequestImpl;
        protected AsyncResponseProcessor<Void> asyncResponseProcessor;
        protected Map<String, String> map;
        protected String timestamp;
        protected String setTimestamp = "";
        protected String dateInString;
        protected long longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
        protected String setContext;
        protected String roleName;
        protected String setRole;

        public SR(String page, AsyncContext asyncContext, String userId, Role role) {
            super(page);
            this.asyncContext = asyncContext;
            request = (HttpServletRequest) asyncContext.getRequest();
            response = (HttpServletResponse) asyncContext.getResponse();
            this.page = page;
            this.userId = userId;
            if (userId != null) {
                myEmail = User.email(db, userId, FactoryRegistry.MAX_TIMESTAMP);
            }
            this.role = role;
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

            roleName = role.roleName();
            setRole = "&role=" + roleName;
            map.put("setRole", setRole);
            map.put("hiddenRole",
                    "<input type=\"hidden\" name=\"role\" value=\"" + roleName + "\" />");
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
                map.put("clearTime", "<a href=\"?from=" + page + "&to=" + page + setContext + setRole + "\">Return to Present Time</a>");
                setTimestamp = "&timestamp=" + timestamp;
                map.put("setTimestamp", setTimestamp + setContext);
                map.put("hiddenTimestamp",
                        "<input type=\"hidden\" name=\"timestamp\" value=\"" + timestamp + "\" />");
                String timestampId = TimestampIds.generate(timestamp);
                map.put("atTime", "at " + SimpleSimon.niceTime(timestampId));
                longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
            }

            if (userId != null) {
                map.put("myEmail", myEmail);
                List<String> roles = new ArrayList<String>();
                roles.add("profile");
                roles.add("maintenance");
                StringBuilder appMenu = new StringBuilder();
                appMenu.append("<a>role &#9660;</a>\n");
                appMenu.append("<ul class=\"sub-menu\">\n");
                for (String rn: roles) {
                    appMenu.append("<li>\n");
                    if (rn.equals(roleName)) {
                        appMenu.append("<a>");
                    } else {
                        appMenu.append("<a href=\"?from=");
                        appMenu.append(page);
                        appMenu.append("&to=");
                        appMenu.append(page);
                        appMenu.append(setTimestamp);
                        appMenu.append(setContext);
                        appMenu.append("&role=");
                        appMenu.append(rn);
                        appMenu.append("\">");
                    }
                    appMenu.append(rn);
                    appMenu.append("</a>");
                    appMenu.append("</li>\n");
                }
                appMenu.append("</ul>\n");
                map.put("guest", appMenu.toString());
            }

            map.put("home", role.menu(request, page, setTimestamp, timestamp, setRole));

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

        protected void println() throws Exception {
            response.getWriter().println(SimpleSimon.replace(servletContext, groupName(), page, map));
        }

        protected void finish() throws Exception {
            println();
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
