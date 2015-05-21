package org.agilewiki.console.requests.maintenance;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.requests.RequestBlade;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.immutable.collections.VersionedMapNodeImpl;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Inverted bi-directional links.
 */
public class InvLinksBlade extends RequestBlade {
    public InvLinksBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String groupName() {
        return "maintenance";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId) {
        new SR(page, asyncContext, userId) {
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
                if (timestamp == null) {
                    map.put("post", "post");
                }
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
                        VersionedMapNodeImpl vmn = (VersionedMapNodeImpl) ma.get(link1Inv);
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
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
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
                map.put("invLinks", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : "");
                map.put("more", hasMore ? "more" : "");
                finish();
            }
        }.signal();
    }
}
