package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.Timestamp;
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
public class FullJournalBlade extends RequestBlade {
    String niceName;
    String prefix = Timestamp.PREFIX;

    public FullJournalBlade(Role role, String page, String niceName) throws Exception {
        super(role, page);
        this.niceName = niceName;
    }

    @Override
    public String niceName() {
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
                map.put("heading", niceName);
                String startingAt = request.getParameter("startingAt");
                if (startingAt == null)
                    startingAt = "";
                if (timestamp != null && startingAt.compareTo(timestamp) < 0)
                    startingAt = timestamp;
                StringBuilder sb;
                boolean hasMore;
                while (true) {
                    try {
                        int limit = 25;
                        hasMore = false;
                        sb = new StringBuilder();
                        PeekABoo<String> idPeekABoo = db.idsIterable(prefix, longTimestamp).iterator();
                        idPeekABoo.setPosition(startingAt);
                        for (String next : idPeekABoo) {
                            String tsId = TimestampIds.generate(next);
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = next;
                                break;
                            }
                            --limit;
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(tsId);
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            String transactionId = "$n" + vmn.getList(NameIds.TRANSACTION_NAME).flatList(longTimestamp).get(0).toString() + ".node";
                            String jeId = TimestampIds.generate(next);
                            sb.append("<a href=\"?from=journal&to=node&nodeId=" + transactionId);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append(setRole + "#rupa\">" + transactionId.substring(2) + "</a>");
                            sb.append(' ');
                            sb.append("<a href=\"?from=journal&to=node&nodeId=" + jeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append(setRole + "#rupa\">" + SimpleSimon.niceTime(tsId) + "</a>");
                            StringBuilder lb = new StringBuilder();
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
                map.put("more", hasMore ? "<img src=\"images/morebutton.jpg\"/>" : "");
                if (hasMore)
                    map.put("setStartingAt", "&startingAt=" +
                            SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD)); //field
                finish();
            }
        }.signal();
    }
}
