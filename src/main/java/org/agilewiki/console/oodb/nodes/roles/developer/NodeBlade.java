package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.*;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import java.util.List;
import java.util.NavigableMap;

/**
 * Request for a journal entry.
 */
public class NodeBlade extends RequestBlade {

    public NodeBlade(Role role, String page) throws Exception {
        super(role, page);
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
                nodeId = request.getParameter("nodeId");
                map.put("nodeId", nodeId);
                if (nodeId.startsWith("$")) {
                    map.put("--node", nodeId.substring(2));
                } else {
                    map.put("--node", nodeId);
                }
                return "&nodeId=" + nodeId;
            }

            @Override
            protected void process()
                    throws Exception {
                if (!SecondaryIds.isNode(db, nodeId, longTimestamp)) {
                    throw new ServletException("not a node");
                }
                String time = null;
                String jeTimestamp = "";
                if (nodeId.startsWith("$t")) {
                    jeTimestamp = TimestampIds.value(nodeId);
                    time = SimpleSimon.niceTime(nodeId);
                }
                String kindId = SecondaryIds.kindId(db, nodeId, longTimestamp);
                String nodeTypeHref = "<a href=\"?from=node&to=node&nodeId=" + kindId + setRole;
                if (timestamp != null) {
                    nodeTypeHref += "&timestamp=" + timestamp;
                }
                nodeTypeHref += "#rupa\">" + kindId.substring(2) + "</a>";
                map.put("nodeType", nodeTypeHref);
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

                        NavigableMap<Comparable, List> atts = ooDb.getFlatMap(nodeId, longTimestamp);
                        if (!atts.isEmpty()) {
                            sb.append("<strong>Attributes:</strong><br />");
                            for (Comparable key : atts.keySet()) {
                                List l = atts.get(key);
                                int sz = l.size();
                                for (int i = 0; i < sz; ++i) {
                                    String s = ((String) key).substring(2) + "[" + i + "] = ";
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s);
                                    String value = l.get(i).toString();
                                    if (!value.startsWith("$") || value.substring(1).indexOf('$') > -1 || !SecondaryIds.isNode(db, value, longTimestamp)) {
                                        sb.append(SimpleSimon.encode(value, s.length() + 4,
                                                SimpleSimon.ENCODE_MULTIPLE_LINES)); //body text
                                    } else {
                                        sb.append("<a href=\"?from=node&to=node&nodeId=");
                                        sb.append(value);
                                        if (timestamp != null) {
                                            sb.append("&timestamp=");
                                            sb.append(timestamp);
                                        }
                                        sb.append(setRole);
                                        sb.append("#rupa\">");
                                        sb.append(value.substring(2));
                                        sb.append("</a>");
                                    }
                                    sb.append("<br />");
                                }
                            }
                        }
                        if (time != null) {
                            sb.append("<strong>Modifies:</strong><br />");
                            for (String nId : Journal.modifies(db, nodeId, longTimestamp)) {
                                String onId = nId;
                                boolean isNode = SecondaryIds.isNode(db, nId, longTimestamp);
                                if (!isNode && SecondaryIds.isNode(db, nId + ".lnk1", longTimestamp)) {
                                    isNode = true;
                                    nId = nId + ".lnk1";
                                }
                                if (!isNode && SecondaryIds.isNode(db, nId + ".key", longTimestamp)) {
                                    isNode = true;
                                    nId = nId + ".key";
                                }
                                if (isNode) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                                    String kId = SecondaryIds.kindId(db, nId, longTimestamp);
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(kId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    sb.append(kId.substring(2));
                                    sb.append("</a>");
                                    sb.append(" ");
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(nId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    if (!onId.startsWith("$t"))
                                        sb.append(onId.substring(2));
                                    else
                                        sb.append(simpleSimon.niceTime(nId));
                                    sb.append("</a>");
                                    sb.append("<br />");
                                } else if (nId.startsWith(SecondaryId.SECONDARY_INV)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;Target: ");
                                    String vnmId = SecondaryId.secondaryInvVmn(nId);
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(vnmId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    if (!vnmId.startsWith("$t"))
                                        sb.append(vnmId.substring(2));
                                    else
                                        sb.append(simpleSimon.niceTime(vnmId));
                                    sb.append("</a>");
                                    sb.append(" Key: ");
                                    String keyId = SecondaryId.secondaryInvType(nId);
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(keyId + ".key");
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    sb.append(keyId.substring(2));
                                    sb.append("</a>");
                                    VersionedMapNode icvmn = db.get(nId);
                                    if (icvmn != null) {
                                        MapAccessor icma = icvmn.mapAccessor(longTimestamp);
                                        for (ListAccessor icla : icma) {
                                            sb.append(" Value: ");
                                            String value = icla.key().toString();
                                            String valueId = value;
                                            boolean isN = SecondaryIds.isNode(db, value, longTimestamp);
                                            if (!isN && SecondaryIds.isNode(db, value + ".lnk1", longTimestamp)) {
                                                valueId = value + ".lnk1";
                                                isN = true;
                                            }
                                            if (!isN)
                                                sb.append(value);
                                            else {
                                                sb.append("<a href=\"?from=");
                                                sb.append(page);
                                                sb.append("&to=node&nodeId=");
                                                sb.append(valueId);
                                                if (timestamp != null) {
                                                    sb.append("&timestamp=");
                                                    sb.append(timestamp);
                                                }
                                                sb.append(setRole + "#rupa\">");
                                                sb.append(value.substring(2));
                                                sb.append("</a>");
                                            }
                                        }
                                    }
                                    sb.append("<br />");
                                } else if (nId.startsWith(SecondaryId.SECONDARY_ID)) {
                                } else if (nId.startsWith(Link1Id.LINK1_ID)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;Origin: ");
                                    String originId = Link1Id.link1IdOrigin(nId);
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(originId);
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    if (!originId.startsWith("$t"))
                                        sb.append(originId.substring(2));
                                    else
                                        sb.append(simpleSimon.niceTime(originId));
                                    sb.append("</a>");
                                    sb.append(" Label: ");
                                    String labelId = Link1Id.link1IdLabel(nId);
                                    sb.append("<a href=\"?from=");
                                    sb.append(page);
                                    sb.append("&to=node&nodeId=");
                                    sb.append(labelId + ".lnk1");
                                    if (timestamp != null) {
                                        sb.append("&timestamp=");
                                        sb.append(timestamp);
                                    }
                                    sb.append(setRole + "#rupa\">");
                                    sb.append(labelId.substring(2));
                                    sb.append("</a>");
                                    VersionedMapNode icvmn = db.get(nId);
                                    if (icvmn != null) {
                                        MapAccessor icma = icvmn.mapAccessor(longTimestamp);
                                        for (ListAccessor icla : icma) {
                                            sb.append(" Destination: ");
                                            String destinationId = icla.key().toString();
                                            sb.append("<a href=\"?from=");
                                            sb.append(page);
                                            sb.append("&to=node&nodeId=");
                                            sb.append(destinationId);
                                            if (timestamp != null) {
                                                sb.append("&timestamp=");
                                                sb.append(timestamp);
                                            }
                                            sb.append(setRole + "#rupa\">");
                                            if (!destinationId.startsWith("$t"))
                                                sb.append(destinationId.substring(2));
                                            else
                                                sb.append(simpleSimon.niceTime(destinationId));
                                            sb.append("</a>");
                                        }
                                    }
                                    sb.append("<br />");
                                } else if (nId.startsWith(Link1Id.LINK1_INV)) {
                                } else if (nId.startsWith(Link1Id.LABEL1_INDEX_ID)) {
                                } else if (nId.startsWith(Link1Id.LABEL1_INDEX_INV)) {
                                }
                            }
                        } else {
                            if (nodeId.endsWith(".key")) {
                                sb.append("<a href=\"?from=node&to=subJournal&subJournal=");
                                sb.append(nodeId.substring(0, nodeId.length() - 4));
                                if (timestamp != null) {
                                    sb.append("&timestamp=" + timestamp);
                                }
                                sb.append(setRole + "\"><strong>Key Journal</strong></a>, ");
                            }
                            if (nodeId.endsWith(".lnk1")) {
                                sb.append("<a href=\"?from=node&to=subJournal&subJournal=");
                                sb.append(nodeId.substring(0, nodeId.length() - 5));
                                if (timestamp != null) {
                                    sb.append("&timestamp=" + timestamp);
                                }
                                sb.append(setRole + "\"><strong>Label Journal</strong></a>, ");
                            }
                            sb.append("<a href=\"?from=node&to=subJournal&subJournal=");
                            sb.append(nodeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=" + timestamp);
                            }
                            sb.append(setRole + "\"><strong>Node Journal</strong></a><br />");
                        }
                        sb.append("<strong>Secondary Keys:</strong><br />");
                        for (String typeId : ooDb.keyIdIterable(nodeId)) {
                            String secondaryInv = SecondaryId.secondaryInv(nodeId, typeId);
                            VersionedMapNode vmn = db.get(secondaryInv);
                            if (vmn != null && !vmn.isEmpty(longTimestamp)) {
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

                                for (String value :
                                        ooDb.keyValueIdIterable(nodeId, typeId, longTimestamp)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;value = ");
                                    if (!SecondaryIds.isNode(db, value, longTimestamp)) {
                                        sb.append(value.substring(2));
                                    } else {
                                        sb.append("<a href=\"?from=node&to=node&nodeId=");
                                        sb.append(value);
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
                        }
                        sb.append("<strong>Links:</strong><br />");
                        for (String typeId : Link1Id.link1LabelIdIterable(db, nodeId)) {
                            String link1Id = Link1Id.link1Id(nodeId, typeId);
                            VersionedMapNode vmn = db.get(link1Id);
                            if (vmn != null && !vmn.isEmpty(longTimestamp)) {
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
                        }
                        sb.append("<strong>Inverted Links:</strong><br />");
                        for (String typeId : Link1Id.link1LabelInvIterable(db, nodeId)) {
                            PeekABoo<String> peekABoo = Link1Id.link1InvIterable(db, nodeId, typeId, longTimestamp);
                            if (peekABoo.hasNext()) {
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
