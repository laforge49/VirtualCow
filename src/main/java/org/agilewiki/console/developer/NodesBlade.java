package org.agilewiki.console.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

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
    protected String niceName() {
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
                            ListAccessor nla = ma.listAccessor(nodeId);
                            if (nla != null) {
                                VersionedMapNode nvmn = (VersionedMapNode) nla.get(0);
                                if (nodeId.startsWith("$t")) {
                                    sb.append(' ');
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
                            nodeId = (String) vmn.higherKey(nodeId, longTimestamp);
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
