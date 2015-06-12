package org.agilewiki.console.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.Role;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class Lnk1OriginsBlade extends RequestBlade {

    String label;
    String niceName;

    public Lnk1OriginsBlade(Role role, String page,
                            String niceName, String label)
            throws Exception {
        super(role, page);
        this.niceName = niceName;
        this.label = label;
    }

    @Override
    protected String niceName() {
        return niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/lnk1Origins";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {

            @Override
            protected void process()
                    throws Exception {
                String labelId = NameIds.generate(label);
                String startingAt = request.getParameter("startingAt");
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        PeekABoo<String> idPeekABoo = Link1Id.label1IdIterable(db, labelId, longTimestamp);
                        if (startingAt != null)
                            idPeekABoo.setPosition(startingAt);
                        for (String id : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = idPeekABoo.getPosition();
                                break;
                            }
                            --limit;
                            String label1IndexId = Link1Id.label1IndexId(NameId.generate(Lnk1OriginsBlade.this.label), id);
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(label1IndexId);
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
                            String line = id;
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            if (nodeId == null) {
                                sb.append("<a href=\"?from=");
                                sb.append(page);
                                sb.append("&to=nodes&secondaryId=");
                                sb.append(label1IndexId);
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
                                sb.append("<a href=\"?from=");
                                sb.append(page);
                                sb.append("&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "#rupa\">");
                                sb.append(nodeId);
                                sb.append("</a>");
                            }
                            sb.append(", <a href=\"?from=");
                            sb.append(page);
                            sb.append("&to=subJournal&subJournal=");
                            sb.append(label1IndexId + setRole);
                            sb.append("#rupa\">journal</a>");
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("lnk1Origins", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                finish();
            }
        }.signal();
    }
}
