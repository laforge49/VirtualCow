package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.console.oodb.nodes.roles.Role;
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
public class SubJournalBlade extends RequestBlade {
    String niceName;

    public SubJournalBlade(Role role, String page, String niceName) throws Exception {
        super(role, page);
        this.niceName = niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/journal";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {

            String subJournal;

            @Override
            protected String setContext() {
                map.put("hiddenContext", "<input type=\"hidden\" name=\"subJournal\" value=\"" + subJournal + "\"/>");
                map.put("subJournal", subJournal);
                return "&subJournal=" + subJournal;
            }

            @Override
            protected void process()
                    throws Exception {
                subJournal = request.getParameter("subJournal");
                String what = null;
                String href = null;
                if (ooDb.isNode(subJournal, longTimestamp)) {
                    what = "";
                    href = subJournal;
                } else if (ooDb.isNode(subJournal + ".lnk1", longTimestamp)) {
                    what = "label ";
                    href = subJournal + ".lnk1";
                } else if (ooDb.isNode(subJournal + ".key", longTimestamp)) {
                    what = "key ";
                    href = subJournal + ".key";
                }
                String heading = niceName + " for " + what + "<a href=\"?from=journal&to=node&nodeId=" + href;
                if (timestamp != null) {
                    heading += "&timestamp=" + timestamp;
                }
                heading += setRole + "#rupa\">" + subJournal.substring(2) + "</a>";
                map.put("heading", heading);
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
                        PeekABoo<String> peekABoo = ooDb.journal(subJournal, longTimestamp);
                        peekABoo.setPosition(TimestampIds.generate(startingAt));
                        for (String jeId : peekABoo) {
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

                            String transactionName = (String) ooDb.get(jeId, NameIds.TRANSACTION_NAME, longTimestamp);
                            sb.append(transactionName + ".node");

                            StringBuilder lb = new StringBuilder();
                            String subject = (String) ooDb.get(jeId, NameIds.SUBJECT, longTimestamp);
                            if (subject!= null) {
                                lb.append(' ');
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            String body = (String) ooDb.get(jeId, NameIds.BODY, longTimestamp);
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
