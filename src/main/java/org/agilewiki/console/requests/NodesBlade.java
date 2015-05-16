package org.agilewiki.console.requests;

import org.agilewiki.console.IdIterable;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.EmptyIterable;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Request for secondary keys.
 */
public class NodesBlade extends RequestBlade {
    public NodesBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
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
                long longTimestamp;
                String secondaryId = request.getParameter("secondaryId");
                if (timestamp != null) {
                    map.put("clearTime", "<a href=\"?from=secondaryKeys&to=secondaryKeys&secondaryId=" + secondaryId +
                            "\">Clear selected time</a>");
                    map.put("formTimestamp", "<input type=\"hidden\" name=\"timestamp\" value=\"" + timestamp + "\"/>");
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
                } else {
                    longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
                    map.put("post", "post");
                }
                String startingAt = request.getParameter("startingAt");
                if (startingAt == null)
                    startingAt = "";
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        MapAccessor ma = db.mapAccessor();
                        ListAccessor la = ma.listAccessor(secondaryId);
                        if (la == null) {
                            break;
                        }
                        VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                        if (vmn == null)
                            break;
                        String nodeId = (String) vmn.ceilingKey(startingAt, longTimestamp);
                        while (limit > 0) {
                            if (nodeId == null)
                                break;
                            --limit;
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = nodeId;
                                break;
                            }
                            sb.append(nodeId);
                            sb.append("<br />");
                            nodeId = (String) vmn.higherKey(nodeId, longTimestamp);
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("nodes", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : ""); //field
                map.put("secondaryId", secondaryId);
                finish();
            }
        }.signal();
    }
}
