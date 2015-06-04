package org.agilewiki.console.developer;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Request for a journal entry.
 */
public class NodeBlade extends RequestBlade {
    public NodeBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String niceName() {
        return "Node";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String nodeId;

            @Override
            protected String setContext() {
                nodeId = request.getParameter("nodeId");
                map.put("nodeId", nodeId);
                return "&nodeId=" + nodeId;
            }

            @Override
            protected void process()
                    throws Exception {
                String time = null;
                String jeTimestamp = "";
                if (nodeId.startsWith("$t")) {
                    jeTimestamp = TimestampIds.value(nodeId);
                    time = SimpleSimon.niceTime(nodeId);
                }
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        MapAccessor ma = db.mapAccessor();

                        ListAccessor la = ma.listAccessor(nodeId);
                        if (la != null) {
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            if (vmn != null && !vmn.isEmpty(longTimestamp)) {
                                sb.append("node id: ");
                                sb.append(nodeId);
                                if (time != null) {
                                    sb.append(" (");
                                    sb.append(time);
                                    sb.append(")");
                                }
                                sb.append("<br />");
                                MapAccessor vma = vmn.mapAccessor(longTimestamp);
                                for (ListAccessor vla : vma) {
                                    int sz = vla.size();
                                    for (int i = 0; i < sz; ++i) {
                                        String s = vla.key() + "[" + i + "] = ";
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s +
                                                SimpleSimon.encode("" + vla.get(i), s.length() + 4,
                                                        SimpleSimon.ENCODE_MULTIPLE_LINES)); //body text
                                        sb.append("<br />");
                                    }
                                }
                                if (time != null) {
                                    sb.append("Modifies: <br />");
                                    for (String nId : Journal.modifies(db, nodeId, longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + nId + "<br />");
                                        if (nId.startsWith(SecondaryId.SECONDARY_INV)) {
                                            VersionedMapNode icvmn = db.get(nId);
                                            if (icvmn != null) {
                                                MapAccessor icma = icvmn.mapAccessor(longTimestamp);
                                                for (ListAccessor icla : icma) {
                                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                                                    sb.append(icla.key());
                                                    sb.append("<br />");
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    sb.append("<a href=\"?from=node&to=subJournal&subJournal=");
                                    sb.append(nodeId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=" + timestamp);
                                    }
                                    sb.append(setRole + "\">Node Journal</a><br />");
                                }
                                sb.append("Secondary Keys: <br />");
                                for (String typeId : SecondaryId.typeIdIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: " + typeId + "<br />");
                                    for (String secondaryId :
                                            SecondaryId.secondaryIdIterable(db, nodeId, typeId, longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;secondaryId - " +
                                                secondaryId + "<br />");
                                    }
                                }
                                sb.append("Links: <br />");
                                for (String typeId : Link1Id.link1LabelIdIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: " + typeId + "<br />");
                                    for (String targetId : Link1Id.link1IdIterable(db, nodeId, typeId, longTimestamp)) {
                                        ListAccessor nla = ma.listAccessor(targetId);
                                        VersionedMapNode nvmn = (VersionedMapNode) nla.get(0);
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                                        sb.append("<a href=\"?from=node&to=node&nodeId=");
                                        sb.append(targetId);
                                        if (timestamp != null) {
                                            sb.append("&timestamp=");
                                            sb.append(timestamp);
                                        }
                                        sb.append(setRole + "\">");
                                        sb.append(targetId);
                                        sb.append("</a>");
                                        if (targetId.startsWith("$t")) {
                                            sb.append(" (");
                                            sb.append(SimpleSimon.niceTime(targetId));
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
                                    }
                                }
                                sb.append("Inverted Links: <br />");
                                for (String typeId : Link1Id.link1LabelInvIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: ");
                                    sb.append("<a href=\"?from=node&to=invLinks&nodeId=");
                                    sb.append(nodeId);
                                    sb.append("&linkType=");
                                    sb.append(typeId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "\">");
                                    sb.append(typeId);
                                    sb.append("</a><br />");
                                }
                            }
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("node", sb.toString());
                if (time != null &&
                        (timestamp == null || !timestamp.equals(jeTimestamp))) {
                    map.put("jeTimestamp", jeTimestamp);
                    map.put("time", "go to " + time);
                    map.put("nice", "(" + time + ")");
                }
                finish();
            }
        }.signal();
    }
}
