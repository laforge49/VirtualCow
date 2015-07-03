package org.agilewiki.vcow.roles.developer;

import org.agilewiki.awdb.db.immutable.collections.PeekABoo;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.TimestampIds;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;

/**
 * Request the journal.
 */
public class FullJournalBlade extends RequestBlade {
    String niceName;

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
                        PeekABoo<String> idPeekABoo = awDb.journal(longTimestamp);
                        idPeekABoo.setPosition(startingAt);
                        for (String next : idPeekABoo) {
                            String tsId = TimestampIds.generate(next);
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = next;
                                break;
                            }
                            --limit;

                            sb.append("<a href=\"?from=journal&to=node&nodeId=" + tsId);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append(setRole + "#rupa\">" + SimpleSimon.niceTime(tsId) + "</a>");

                            sb.append(' ');

                            String transactionName = (String) awDb.get(tsId, NameIds.TRANSACTION_NAME, longTimestamp);
                            sb.append(transactionName);

                            StringBuilder lb = new StringBuilder();
                            String subject = (String) awDb.get(tsId, NameIds.SUBJECT, longTimestamp);
                            if (subject != null) {
                                lb.append(' ');
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            String body = (String) awDb.get(tsId, NameIds.BODY, longTimestamp);
                            if (body != null) {
                                if (subject != null) {
                                    lb.append(" | ");
                                }
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
