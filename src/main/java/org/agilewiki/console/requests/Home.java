package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;

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
public class Home {

    public static void getHome(ServletContext servletContext,
                               HttpServletRequest request,
                               HttpServletResponse response) {
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
                return;
            }
            map.put("setTimestamp", "&timestamp=" + timestamp);
            map.put("setStartingAt", "&startingAt=" + timestamp);
            map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
            map.put("clearTime", "<a href=\".\">Clear selected time</a>");
            response.getWriter().println(SimpleSimon.replace(servletContext, "home", map));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            servletContext.log("home", ex);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
            }
        }
    }
}
