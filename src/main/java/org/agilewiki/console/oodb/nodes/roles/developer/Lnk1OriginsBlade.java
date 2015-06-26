package org.agilewiki.console.oodb.nodes.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
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
    public String niceName() {
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
                        for (String nodeId : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = idPeekABoo.getPosition();
                                break;
                            }
                            String kindId = ooDb.kindId(nodeId, longTimestamp);
                            if (kindId == null)
                                continue;
                            --limit;
                            MapAccessor ma = db.mapAccessor();

                            sb.append(kindId.substring(2));

                            sb.append(' ');

                            sb.append("<a href=\"?from=");
                            sb.append(page);
                            sb.append("&to=node&nodeId=");
                            sb.append(nodeId);
                            if (timestamp != null) {
                                sb.append("&timestamp=");
                                sb.append(timestamp);
                            }
                            sb.append(setRole + "#rupa\">");
                            if (!nodeId.startsWith("$t"))
                                sb.append(nodeId.substring(2));
                            else
                                sb.append(simpleSimon.niceTime(nodeId));
                            sb.append("</a>");
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
