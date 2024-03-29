package org.agilewiki.vcow.roles.developer;

import org.agilewiki.awdb.Node;
import org.agilewiki.awdb.db.ids.composites.Link1Id;
import org.agilewiki.awdb.db.ids.composites.SecondaryId;
import org.agilewiki.awdb.db.immutable.FactoryRegistry;
import org.agilewiki.awdb.db.immutable.collections.PeekABoo;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;
import org.agilewiki.awdb.nodes.Key_NodeFactory;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.TimestampIds;
import org.agilewiki.vcow.roles.Role;

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
                StringBuilder sb;
                String time;
                String jeTimestamp;
                while (true) {
                    try {
                        time = null;
                        if (!awDb.isNode(nodeId, longTimestamp)) {
                            throw new ServletException("not a node");
                        }
                        jeTimestamp = "";
                        if (nodeId.startsWith("$t")) {
                            jeTimestamp = TimestampIds.value(nodeId);
                            time = SimpleSimon.niceTime(nodeId);
                        }
                        String kindId = awDb.kindId(nodeId, longTimestamp);
                        String nodeTypeHref = "<a href=\"?from=node&to=node&nodeId=" + kindId + setRole;
                        if (timestamp != null) {
                            nodeTypeHref += "&timestamp=" + timestamp;
                        }
                        nodeTypeHref += "#rupa\">" + kindId.substring(2) + "</a>";
                        map.put("nodeType", nodeTypeHref);
                        sb = new StringBuilder();
                        Node node = awDb.fetchNode(nodeId, longTimestamp);
                        sb.append("Realm: <a href=\"?from=node&to=node&nodeId=");
                        sb.append(node.getRealmId());
                        if (timestamp != null) {
                            sb.append("&timestamp=");
                            sb.append(timestamp);
                        }
                        sb.append(setRole);
                        sb.append("#rupa\"><strong>");
                        sb.append(node.getRealmId().substring(2));
                        sb.append("</strong></a><br />");
                        if (nodeId.startsWith("$n")) {
                            if (nodeId.endsWith(".key")) {
                                if (awDb.keyHasValueId(nodeId.substring(0, nodeId.length() - 4), longTimestamp)) {
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
                                PeekABoo<String> idPeekABoo = awDb.originIdIterable(label, longTimestamp);
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
                                idPeekABoo = awDb.destinationIdIterable(label, longTimestamp);
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
                                if (awDb.keyHasTargetId(Key_NodeFactory.SUPERTYPE_ID, nodeId, longTimestamp)) {
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
                                if (awDb.keyHasTargetId(Key_NodeFactory.NODETYPE_ID, nodeId, longTimestamp)) {
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

                        NavigableMap<Comparable, List> atts = awDb.getFlatMap(nodeId, longTimestamp);
                        if (!atts.isEmpty()) {
                            sb.append("<strong>Attributes:</strong><br />");
                            for (Comparable key : atts.keySet()) {
                                List l = atts.get(key);
                                int sz = l.size();
                                for (int i = 0; i < sz; ++i) {
                                    String s = ((String) key).substring(2) + "[" + i + "] = ";
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s);
                                    String value = l.get(i).toString();
                                    if (!value.startsWith("$") || value.substring(1).indexOf('$') > -1 || !awDb.isNode(value, longTimestamp)) {
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
                            for (String nId : awDb.modifies(nodeId, longTimestamp)) {
                                String onId = nId;
                                boolean isNode = awDb.isNode(nId, longTimestamp);
                                if (!isNode && awDb.isNode(nId + ".lnk1", longTimestamp)) {
                                    isNode = true;
                                    nId = nId + ".lnk1";
                                }
                                if (!isNode && awDb.isNode(nId + ".key", longTimestamp)) {
                                    isNode = true;
                                    nId = nId + ".key";
                                }
                                if (isNode) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                                    String kId = awDb.kindId(nId, longTimestamp);
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
                                    boolean isVN = awDb.isNode(vnmId, longTimestamp);
                                    if (isVN) {
                                        sb.append("<a href=\"?from=");
                                        sb.append(page);
                                        sb.append("&to=node&nodeId=");
                                        sb.append(vnmId);
                                        if (timestamp != null) {
                                            sb.append("&timestamp=");
                                            sb.append(timestamp);
                                        }
                                        sb.append(setRole + "#rupa\">");
                                    }
                                    if (!vnmId.startsWith("$t"))
                                        sb.append(vnmId.substring(2));
                                    else
                                        sb.append(simpleSimon.niceTime(vnmId));
                                    if (isVN) {
                                        sb.append("</a>");
                                    }
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
                                    if (isVN) {
                                        for (String value : awDb.nodeValueIdIterable(vnmId, keyId, longTimestamp)) {
                                            sb.append(" Value: ");
                                            String valueId = value;
                                            boolean isN = awDb.isNode(value, longTimestamp);
                                            if (!isN && awDb.isNode(value + ".lnk1", longTimestamp)) {
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
                                    boolean isON = awDb.isNode(originId, longTimestamp);
                                    if (isON) {
                                        sb.append("<a href=\"?from=");
                                        sb.append(page);
                                        sb.append("&to=node&nodeId=");
                                        sb.append(originId);
                                        if (timestamp != null) {
                                            sb.append("&timestamp=");
                                            sb.append(timestamp);
                                        }
                                        sb.append(setRole + "#rupa\">");
                                    }
                                    if (!originId.startsWith("$t"))
                                        sb.append(originId.substring(2));
                                    else
                                        sb.append(simpleSimon.niceTime(originId));
                                    if (isON) {
                                        sb.append("</a>");
                                    }
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
                                    if (isON) {
                                        for (String destinationId : awDb.destinationIdIterable(originId, labelId, longTimestamp)) {
                                            sb.append(" Destination: ");
                                            boolean isDN = awDb.isNode(destinationId, longTimestamp);
                                            if (isDN) {
                                                sb.append("<a href=\"?from=");
                                                sb.append(page);
                                                sb.append("&to=node&nodeId=");
                                                sb.append(destinationId);
                                                if (timestamp != null) {
                                                    sb.append("&timestamp=");
                                                    sb.append(timestamp);
                                                }
                                                sb.append(setRole + "#rupa\">");
                                            }
                                            if (!destinationId.startsWith("$t"))
                                                sb.append(destinationId.substring(2));
                                            else
                                                sb.append(simpleSimon.niceTime(destinationId));
                                            if (isDN) {
                                                sb.append("</a>");
                                            }
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
                        for (String typeId : awDb.nodeKeyIdIterable(nodeId, FactoryRegistry.MAX_TIMESTAMP)) {
                            if (awDb.nodeHasKeyId(nodeId, typeId, longTimestamp)) {
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
                                        awDb.nodeValueIdIterable(nodeId, typeId, longTimestamp)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;value = ");
                                    if (!awDb.isNode(value, longTimestamp)) {
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
                        for (String typeId : awDb.originLabelIdIterable(nodeId, FactoryRegistry.MAX_TIMESTAMP)) {
                            if (awDb.hasLabel1(nodeId, typeId, longTimestamp)) {
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

                                for (String targetId : awDb.destinationIdIterable(nodeId, typeId, longTimestamp)) {
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
                                    if (targetId.startsWith("$t")) {
                                        sb.append(" (");
                                        sb.append(SimpleSimon.niceTime(targetId));
                                        sb.append(") ");
                                        String transactionName = (String) awDb.get(targetId, NameIds.TRANSACTION_NAME, longTimestamp);
                                        sb.append(transactionName);
                                    }
                                    StringBuilder lb = new StringBuilder();
                                    String subject = (String) awDb.get(targetId, NameIds.TRANSACTION_NAME, longTimestamp);
                                    if (subject != null) {
                                        lb.append(' ');
                                        lb.append(subject);
                                        lb.append(" | ");
                                    }
                                    String body = (String) awDb.get(targetId, NameIds.BODY, longTimestamp);
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
                                    sb.append(line);
                                    sb.append("<br />");
                                }
                            }
                        }
                        sb.append("<strong>Inverted Links:</strong><br />");
                        for (String typeId : awDb.targetLabelInvIterable(nodeId)) {
                            PeekABoo<String> peekABoo = awDb.originIdIterable(nodeId, typeId, longTimestamp);
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
