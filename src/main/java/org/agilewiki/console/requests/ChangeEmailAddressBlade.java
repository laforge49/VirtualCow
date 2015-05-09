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
 * Change the email address of the user.
 */
public class ChangeEmailAddressBlade extends RequestBlade {
    public ChangeEmailAddressBlade(ServletContext servletContext, Db db) throws Exception {
        super(servletContext, db);
    }

    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                finish();
            }
        }.signal();
    }
}
