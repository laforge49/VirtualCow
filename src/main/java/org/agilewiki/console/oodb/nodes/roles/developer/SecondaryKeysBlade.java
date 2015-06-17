package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SecondaryIds;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class SecondaryKeysBlade extends RequestBlade {

    String secondaryType;
    String niceName;

    public SecondaryKeysBlade(Role role, String page,
                              String niceName, String secondaryType)
            throws Exception {
        super(role, page);
        this.niceName = niceName;
        this.secondaryType = secondaryType;
    }

    @Override
    public String niceName() {
        return niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/secondaryKeys";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {

            @Override
            protected void process()
                    throws Exception {
                String prefix = SecondaryId.SECONDARY_ID + NameIds.generate(secondaryType);
                String startingAt = request.getParameter("startingAt");
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        PeekABoo<String> idPeekABoo = db.idsIterable(prefix, longTimestamp);
                        if (startingAt != null)
                            idPeekABoo.setPosition(startingAt);
                        for (String id : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = id;
                                break;
                            }
                            --limit;
                            String secondaryId = SecondaryId.secondaryId(NameId.generate(secondaryType), id);
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(secondaryId);
                            String nodeId = null;
                            if (la != null) {
                                VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                                if (vmn != null) {
                                    String first = (String) vmn.firstKey(longTimestamp);
                                    String last = (String) vmn.lastKey(longTimestamp);
                                    if (first.equals(last)) {
                                        nodeId = first;
                                    }
                                }
                            }
                            String line = id.substring(2);
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            if (nodeId == null) {
                                sb.append("<a href=\"?from=");
                                sb.append(page);
                                sb.append("&to=nodes&secondaryId=");
                                sb.append(secondaryId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "#rupa\">");
                                sb.append(line);
                                sb.append("</a>");
                            } else {
                                sb.append(line);
                                sb.append(" -> ");
                                String kindId = SecondaryIds.kindId(db, nodeId, longTimestamp);
                                sb.append("<a href=\"?from=");
                                sb.append(page);
                                sb.append("&to=node&nodeId=");
                                sb.append(kindId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "#rupa\">");
                                sb.append(kindId.substring(2));
                                sb.append("</a> ");
                                sb.append("<a href=\"?from=");
                                sb.append(page);
                                sb.append("&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "#rupa\">");
                                if (nodeId.startsWith("$t")) {
                                    sb.append(simpleSimon.niceTime(nodeId));
                                } else
                                    sb.append(nodeId.substring(2));
                                sb.append("</a>");
                            }
                            sb.append(", <a href=\"?from=");
                            sb.append(page);
                            sb.append("&to=subJournal&subJournal=");
                            sb.append(secondaryId + setRole);
                            sb.append("#rupa\">journal</a>");
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("secondaryKeys", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                finish();
            }
        }.signal();
    }
}
