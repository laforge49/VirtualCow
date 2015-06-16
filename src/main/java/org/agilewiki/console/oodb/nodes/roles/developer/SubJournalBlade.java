package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.*;
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
                if (SecondaryIds.isNode(db, subJournal, longTimestamp)) {
                    what = "";
                    href = subJournal;
                } else if (SecondaryIds.isNode(db, subJournal + ".lnk1", longTimestamp)) {
                    what = "label ";
                    href = subJournal + ".lnk1";
                } else if (SecondaryIds.isNode(db, subJournal + ".key", longTimestamp)) {
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
                        PeekABoo<String> peekABoo = Journal.journal(db, subJournal, longTimestamp);
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
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(jeId);
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            String transactionId = "$n" + vmn.getList(NameIds.TRANSACTION_NAME).flatList(longTimestamp).get(0).toString() + ".node";
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
                            sb.append(setRole + "#rupa\">" + SimpleSimon.niceTime(jeId) + "</a>");
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