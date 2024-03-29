package org.agilewiki.vcow.roles.developer;

import org.agilewiki.awdb.db.immutable.collections.PeekABoo;
import org.agilewiki.awdb.db.virtualcow.UnexpectedChecksumException;
import org.agilewiki.vcow.NameIds;
import org.agilewiki.vcow.RequestBlade;
import org.agilewiki.vcow.SimpleSimon;
import org.agilewiki.vcow.roles.Role;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class Lnk1DestinationsBlade extends RequestBlade {

    String label;
    String niceName;

    public Lnk1DestinationsBlade(Role role, String page,
                                 String niceName, String label)
            throws Exception {
        super(role, page);
        this.niceName = niceName;
        this.label = label.substring(2);
    }

    @Override
    public String niceName() {
        return niceName;
    }

    @Override
    protected String fileName(String roleName, String page) {
        return "developer/lnk1Destinations";
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
                        PeekABoo<String> idPeekABoo = awDb.destinationIdIterable(labelId, longTimestamp);
                        if (startingAt != null)
                            idPeekABoo.setPosition(startingAt);
                        for (String nodeId : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = idPeekABoo.getPosition();
                                break;
                            }
                            --limit;
                            String kindId = awDb.kindId(nodeId, longTimestamp);

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
                map.put("lnk1Destinations", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                finish();
            }
        }.signal();
    }
}
