package org.agilewiki.console.requests;

import org.agilewiki.console.IdIterable;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
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
public class SecondaryKeysBlade extends RequestBlade {
    public SecondaryKeysBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
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
                String secondaryType = request.getParameter("secondaryType");
                String keyPrefix = request.getParameter("keyPrefix");
                if (timestamp != null) {
                    map.put("clearTime", "<a href=\"?from=secondaryKeys&to=secondaryKeys&secondaryType=" + secondaryType +
                            "&keyPrefix=" + keyPrefix +
                            "\">Return to Present Time</a>");
                    map.put("formTimestamp", "<input type=\"hidden\" name=\"timestamp\" value=\"" + timestamp + "\"/>");
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
                } else {
                    longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
                    map.put("post", "post");
                }
                String prefix = SecondaryId.SECONDARY_ID + NameIds.generate(secondaryType);
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
                        for (String id : new IdIterable(servletContext, db, prefix, keyPrefix + startingAt, longTimestamp)) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = ValueId.value(id);
                                break;
                            }
                            --limit;
                            String secondaryId = SecondaryId.secondaryId(NameId.generate(secondaryType), id);
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(secondaryId);
                            String nodeId = null;
                            if (la != null) {
                                VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                                if (vmn != null) {
                                    String first = (String) vmn.firstKey(longTimestamp);
                                    String last = (String) vmn.lastKey(longTimestamp);
                                    if (first.equals(last)) {
                                        nodeId = first;
                                    }
                                }
                            }
                            String line = id;
                            line = line.replaceAll("\r", "");
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            if (nodeId == null) {
                                sb.append("<a href=\"?from=secondaryKeys&to=nodes&secondaryId=");
                                sb.append(secondaryId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append("\">");
                                sb.append(line);
                                sb.append("</a>");
                            } else {
                                sb.append(line);
                                sb.append(" -> ");
                                sb.append("<a href=\"?from=secondaryKeys&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append("\">");
                                sb.append(nodeId);
                                sb.append("</a>");
                            }
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("secondaryKeys", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                map.put("secondaryType", secondaryType);
                map.put("keyPrefix", keyPrefix);
                finish();
            }
        }.signal();
    }
}
