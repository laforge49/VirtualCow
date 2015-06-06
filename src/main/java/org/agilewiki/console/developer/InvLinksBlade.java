package org.agilewiki.console.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Inverted bi-directional links.
 */
public class InvLinksBlade extends RequestBlade {
    public InvLinksBlade(SimpleSimon simpleSimon, String page) throws Exception {
        super(simpleSimon, page);
    }

    @Override
    protected String niceName() {
        return "Inverted Links";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String targetId;
            String labelId;

            @Override
            protected String setContext() {
                targetId = request.getParameter("nodeId");
                labelId = request.getParameter("linkType");
                map.put("nodeId", targetId);
                map.put("linkType", labelId);
                return "&nodeId=" + targetId +
                        "&linkType=" + labelId;
            }

            @Override
            protected void process()
                    throws Exception {
                String link1Inv = Link1Id.link1Inv(targetId, labelId);
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
                        PeekABoo<String> peekABoo = Link1Id.link1InvIterable(db, targetId, labelId, longTimestamp);
                        peekABoo.setPosition(startingAt);
                        for (String nodeId : peekABoo) {
                            --limit;
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = nodeId;
                                break;
                            }
                            sb.append("<a href=\"?from=nodes&to=node&nodeId=");
                            sb.append(nodeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole + "\">");
                            sb.append(nodeId);
                            sb.append("</a>");
                            ListAccessor nla = ma.listAccessor(nodeId);
                            if (nla != null) {
                                VersionedMapNode nvmn = (VersionedMapNode) nla.get(0);
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
                            }
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("invLinks", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : "");
                map.put("more", hasMore ? "<img src=\"images/morebutton.jpg\"/>" : "");
                finish();
            }
        }.signal();
    }
}
