package org.agilewiki.console.requests;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
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
import java.util.List;

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
                    map.put("clearTime", "<a href=\"?from=nodes&to=nodes&secondaryId=" + secondaryId +
                            "\">Return to Present Time</a>");
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
                            ListAccessor nla = ma.listAccessor(nodeId);
                            VersionedMapNode nvmn = (VersionedMapNode) nla.get(0);
                            sb.append("<a href=\"?from=nodes&to=node&nodeId=");
                            sb.append(nodeId);
                            sb.append("\">");
                            sb.append(nodeId);
                            sb.append("</a>");
                            if (nodeId.startsWith("$t")) {
                                sb.append(" (");
                                sb.append(SimpleSimon.niceTime(nodeId));
                                sb.append(") ");
                                String transactionName = nvmn.getList(NameIds.TRANSACTION_NAME).flatList(longTimestamp).get(0).toString();
                                sb.append(transactionName);
                            }
                            StringBuilder lb = new StringBuilder();
                            List subjectList = nvmn.getList(NameIds.SUBJECT).flatList(longTimestamp);
                            if (subjectList.size() > 0) {
                                lb.append(' ');
                                String subject = subjectList.get(0).toString();
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            List bodyList = nvmn.getList(NameIds.BODY).flatList(longTimestamp);
                            if (bodyList.size() > 0) {
                                if (subjectList.size() == 0) {
                                    lb.append(" | ");
                                }
                                String body = bodyList.get(0).toString();
                                lb.append(body);
                            }
                            String line = lb.toString();
                            line = line.replace("\r", "");
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            sb.append(line);
                            sb.append("<br />");
                            nodeId = (String) vmn.higherKey(nodeId, longTimestamp);
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("nodes", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : "");
                map.put("more", hasMore ? "more" : "");
                map.put("secondaryId", secondaryId);
                finish();
            }
        }.signal();
    }
}
