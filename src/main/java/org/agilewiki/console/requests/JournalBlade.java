package org.agilewiki.console.requests;

import org.agilewiki.console.IdIterable;
import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Request the journal.
 */
public class JournalBlade extends RequestBlade {
    public JournalBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String timestamp = request.getParameter("timestamp");
                long longTimestamp;
                if (timestamp != null) {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    String timestampId = TimestampIds.generate(timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(timestampId));
                }
                longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
                String prefix = Timestamp.PREFIX;
                String startingAt = request.getParameter("startingAt");
                if (startingAt == null)
                    startingAt = "";
                StringBuilder sb;
                boolean hasMore;
                while (true) {
                    try {
                        int limit = 25;
                        hasMore = false;
                        sb = new StringBuilder();
                        for (String next : new IdIterable(servletContext, db, prefix, startingAt, longTimestamp)) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = next;
                                break;
                            }
                            --limit;
                            String tsId = TimestampIds.generate(next);
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(tsId);
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            sb.append("<a href=\"?from=journal&to=journalEntry&jeTimestamp=" + next);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append("\">" + SimpleSimon.niceTime(tsId) + "</a>");
                            sb.append(' ');
                            StringBuilder lb = new StringBuilder();
                            String transactionName = vmn.getList(NameIds.TRANSACTION_NAME).flatList(longTimestamp).get(0).toString();
                            lb.append(transactionName);
                            List subjectList = vmn.getList(NameIds.SUBJECT).flatList(longTimestamp);
                            if (subjectList.size() > 0) {
                                lb.append(' ');
                                String subject = subjectList.get(0).toString();
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            List bodyList = vmn.getList(NameIds.BODY).flatList(longTimestamp);
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
                            sb.append("<font style=\"font-family:courier\">");
                            sb.append(line);
                            sb.append("</font>");
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("journal", sb.toString());
                map.put("more", hasMore ? "more" : "");
                if (hasMore)
                    map.put("setStartingAt", "&startingAt=" +
                            SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD)); //field
                finish();
            }
        }.signal();
    }
}
