package org.agilewiki.console.developer;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import java.util.List;

/**
 * Request for a journal entry.
 */
public class NodeBlade extends RequestBlade {
    String niceName;
    String bNodeBlade;

    public NodeBlade(Role role, String page, String niceName, String nodeBlade) throws Exception {
        super(role, page);
        bNodeBlade = nodeBlade;
        this.niceName = niceName;
    }

    @Override
    protected String niceName() {
        return niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/node";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            String nodeId;

            @Override
            protected String setContext() {
                if (bNodeBlade == null)
                    nodeId = request.getParameter("nodeId");
                else
                    nodeId = bNodeBlade;
                map.put("nodeId", nodeId);
                map.put("--node", nodeId.substring(2));
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
                        if (nodeId.startsWith("$n")) {
                            if (nodeId.endsWith(".key")) {
                                String prefix = SecondaryId.SECONDARY_ID + nodeId.substring(0, nodeId.length() - 4);
                                PeekABoo<String> idPeekABoo = db.idsIterable(prefix, longTimestamp);
                                if (idPeekABoo.hasNext()) {
                                    sb.append("<a href=\"?from=node&to=");
                                    sb.append(nodeId.substring(2).replace(".key", "Values"));
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("#rupa\"><strong>Values</strong></a><br />");
                                }
                            } else if (nodeId.endsWith(".lnk1")) {
                                String label = nodeId.substring(0, nodeId.length() - 5);
                                PeekABoo<String> idPeekABoo = Link1Id.label1IdIterable(db, label, longTimestamp);
                                if (idPeekABoo.hasNext()) {
                                    sb.append("<a href=\"?from=node&to=");
                                    sb.append(nodeId.substring(2).replace(".lnk1", "Origins"));
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("#rupa\"><strong>Origins</strong></a>, ");
                                } else
                                    sb.append("Origins, ");
                                idPeekABoo = Link1Id.label1InvIterable(db, label, longTimestamp);
                                if (idPeekABoo.hasNext()) {
                                    sb.append("<a href=\"?from=node&to=");
                                    sb.append(nodeId.substring(2).replace(".lnk1", "Destinations"));
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("#rupa\"><strong>Destinations</strong></a><br />");
                                } else
                                    sb.append("Destinations<br />");
                            } else if (nodeId.endsWith(".node")) {
                                VersionedMapNode vmn = (VersionedMapNode) ma.get("$D$nsuperType" + nodeId);
                                if (vmn != null && vmn.firstKey(longTimestamp) != null) {
                                    sb.append("<a href=\"?from=node&to=nodes&secondaryId=$D$nsuperType");
                                    sb.append(nodeId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("#rupa\"><strong>Subtypes</strong></a>, ");
                                } else
                                    sb.append("Subtypes, ");
                                vmn = (VersionedMapNode) ma.get("$D$nnodeType" + nodeId);
                                if (vmn != null && vmn.firstKey(longTimestamp) != null) {
                                    sb.append("<a href=\"?from=node&to=nodes&secondaryId=$D$nnodeType");
                                    sb.append(nodeId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("#rupa\"><strong>Instances</strong></a><br />");
                                } else
                                    sb.append("Instances<br />");
                            }
                        }

                        ListAccessor la = ma.listAccessor(nodeId);
                        if (la != null) {
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            if (vmn != null && !vmn.isEmpty(longTimestamp)) {
                                MapAccessor vma = vmn.mapAccessor(longTimestamp);
                                sb.append("<strong>Attributes:</strong><br />");
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
                            }
                        }
                        if (time != null) {
                            sb.append("<strong>Modifies:</strong><br />");
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
                            sb.append(setRole + "\"><strong>Node Journal</strong></a><br />");
                        }
                        sb.append("<strong>Secondary Keys:</strong><br />");
                        for (String typeId : SecondaryId.typeIdIterable(db, nodeId)) {
                            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;key: <a href=\"?from=node&to=node&nodeId=");
                            sb.append(typeId);
                            sb.append(".key");
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole);
                            sb.append("\">");
                            sb.append(typeId.substring(2));
                            sb.append("</a><br />");
                            for (String secondaryId :
                                    SecondaryId.secondaryIdIterable(db, nodeId, typeId, longTimestamp)) {
                                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;value = ");
                                String value = SecondaryId.secondaryIdValue(secondaryId);
                                String valueNode = null;
                                for (String nt : SecondaryId.secondaryIdIterable(db, value, "$nnodeType", longTimestamp)) {
                                    valueNode = value;
                                }
                                if (valueNode == null && value.startsWith("$")) {
                                    for (String nt : SecondaryId.secondaryIdIterable(db, value + ".node", "$nnodeType", longTimestamp)) {
                                        valueNode = value + ".node";
                                    }
                                }
                                if (valueNode == null)
                                    sb.append(value);
                                else {
                                    sb.append("<a href=\"?from=node&to=node&nodeId=");
                                    sb.append(valueNode);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole);
                                    sb.append("\">");
                                    sb.append(value.substring(2));
                                    sb.append("</a>");
                                }
                                sb.append("<br />");
                            }
                        }
                        sb.append("<strong>Links:</strong><br />");
                        for (String typeId : Link1Id.link1LabelIdIterable(db, nodeId)) {
                            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;label: <a href=\"?from=node&to=node&nodeId=");
                            sb.append(typeId);
                            sb.append(".lnk1");
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole);
                            sb.append("\">");
                            sb.append(typeId.substring(2));
                            sb.append("</a><br />");
                            for (String targetId : Link1Id.link1IdIterable(db, nodeId, typeId, longTimestamp)) {
                                sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                                sb.append("<a href=\"?from=node&to=node&nodeId=");
                                sb.append(targetId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "\">");
                                sb.append(targetId.substring(2));
                                sb.append("</a>");
                                ListAccessor nla = ma.listAccessor(targetId);
                                if (nla != null) {
                                    VersionedMapNode nvmn = (VersionedMapNode) nla.get(0);
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
                                }
                                sb.append("<br />");
                            }
                        }
                        sb.append("<strong>Inverted Links:</strong><br />");
                        for (String typeId : Link1Id.link1LabelInvIterable(db, nodeId)) {
                            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;label: ");
                            sb.append("<a href=\"?from=node&to=invLinks&nodeId=");
                            sb.append(nodeId);
                            sb.append("&linkType=");
                            sb.append(typeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole + "\">");
                            sb.append(typeId.substring(2));
                            sb.append("</a><br />");
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
