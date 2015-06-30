package org.agilewiki.console.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class NodesBlade extends RequestBlade {
    public final String niceName;
    public final String secondaryId;

    public NodesBlade(Role role, String page) throws Exception {
        this(role, page, null, null);
    }

    public NodesBlade(Role role, String page, String niceName, String secondaryId) throws Exception {
        super(role, page);
        this.niceName = niceName;
        this.secondaryId = secondaryId;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/nodes";
    }

    @Override
    public String niceName() {
        if (niceName == null)
            return "Nodes";
        return niceName;
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String secondaryId;

            @Override
            protected String setContext() {
                if (NodesBlade.this.secondaryId == null)
                    secondaryId = request.getParameter("secondaryId");
                else
                    secondaryId = NodesBlade.this.secondaryId;
                String keyId = SecondaryId.secondaryIdType(secondaryId);
                String valueId = SecondaryId.secondaryIdValue(secondaryId);
                map.put("keyId", keyId + ".key");
                map.put("--keyId", keyId.substring(2));
                map.put("valueId", valueId);
                map.put("--valueId", valueId.substring(2));
                map.put("secondaryId", secondaryId);
                return "&secondaryId=" + secondaryId;
            }

            @Override
            protected void process()
                    throws Exception {
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
                        String keyId = SecondaryId.secondaryIdType(secondaryId);
                        String valueId = SecondaryId.secondaryIdValue(secondaryId);
                        for (String nodeId : ooDb.keyTargetIdIterable(keyId, valueId, longTimestamp)) {
                            --limit;
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = nodeId;
                                break;
                            }

                            String kindId = ooDb.kindId(nodeId, longTimestamp);
                            sb.append(kindId.substring(2));

                            sb.append(' ');

                            sb.append("<a href=\"?from=nodes&to=node&nodeId=");
                            sb.append(nodeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole + "\">");
                            if (nodeId.startsWith("$t")) {
                                sb.append(SimpleSimon.niceTime(nodeId));
                            } else
                                sb.append(nodeId.substring(2));
                            sb.append("</a>");
                            StringBuilder lb = new StringBuilder();
                            String subject = (String) ooDb.get(nodeId, NameIds.SUBJECT, longTimestamp);
                            if (subject != null) {
                                lb.append(' ');
                                lb.append(subject);
                                lb.append(" | ");
                            }
                            String body = (String) ooDb.get(nodeId, NameIds.BODY, longTimestamp);
                            if (body != null) {
                                if (subject == null) {
                                    lb.append(" | ");
                                }
                                lb.append(body);
                            }
                            String line = lb.toString();
                            line = line.replace("\r", "");
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            sb.append(line);
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("nodes", sb.toString());
                map.put("setStartingAt", hasMore ? "&startingAt=" + startingAt : "");
                map.put("more", hasMore ? "<img src=\"images/morebutton.jpg\"/>" : "");
                finish();
            }
        }.signal();
    }
}
