package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Request for home page.
 */
public class HomeBlade extends RequestBlade {
    public HomeBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String timestamp = request.getParameter("timestamp");
                if (timestamp == null) {
                    map.put("post", "post");
                } else {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                }
                finish();
            }
        }.signal();
    }
}
