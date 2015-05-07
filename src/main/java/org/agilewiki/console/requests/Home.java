package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.utils.virtualcow.Db;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Request for home page.
 */
public class Home extends RequestBlade {
    public Home(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
    }

    public void getHome(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
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
                } else {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("setStartingAt", "&startingAt=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    map.put("clearTime", "<a href=\".\">Clear selected time</a>");
                }
                finish();
            }
        }.signal();
    }
}
