package org.agilewiki.console.developer;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.Timestamp;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Request the journal.
 */
public class JournalBlade extends RequestBlade {
    String niceName;
    String id;

    public JournalBlade(SimpleSimon simpleSimon, String niceName, String id) throws Exception {
        super(simpleSimon);
        this.niceName = niceName;
        this.id = id;
    }

    @Override
    protected String niceName() {
        return niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/journal";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String startingAt = request.getParameter("startingAt");
                if (startingAt == null)
                    startingAt = "";
                if (timestamp != null && startingAt.compareTo(timestamp) < 0)
                    startingAt = timestamp;
                StringBuilder sb;
                boolean hasMore;
                while (true) {
                    try {
                        hasMore = false;
                        sb = new StringBuilder();
                        int limit = 25;
                        PeekABoo<String> peekABoo = Journal.journal(db, id, longTimestamp);
                        peekABoo.setPosition(TimestampIds.generate(startingAt));
                        for (String jeId: peekABoo) {
                            if (jeId == null)
                                break;
                            String next = TimestampIds.value(jeId);
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = next;
                                break;
                            }
                            --limit;
                            sb.append("<a href=\"?from=journal&to=node&nodeId=" + jeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append(setRole + "#rupa\">" + SimpleSimon.niceTime(jeId) + "</a>");
                            sb.append(' ');
                            StringBuilder lb = new StringBuilder();
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(jeId);
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
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
//                            jeId = (String) jvmn.higherKey(jeId, longTimestamp);
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("journal", sb.toString());
                map.put("more", hasMore ? "<img src=\"images/morebutton.jpg\"/>" : "");
                if (hasMore)
                    map.put("setStartingAt", "&startingAt=" +
                            SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD)); //field
                finish();
            }
        }.signal();
    }
}
