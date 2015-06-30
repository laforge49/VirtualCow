package org.agilewiki.console.roles.developer;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.awdb.nodes.Key_NodeFactory;
import org.agilewiki.console.roles.Role;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.PeekABoo;
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
                String startingAt = request.getParameter("startingAt");
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        String keyId = NameIds.generate(secondaryType);
                        PeekABoo<String> idPeekABoo = awDb.keyValueIdIterable(keyId, longTimestamp);
                        if (startingAt != null)
                            idPeekABoo.setPosition(startingAt);
                        for (String valueId : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = valueId;
                                break;
                            }
                            --limit;
                            String secondaryId = SecondaryId.secondaryId(keyId, valueId);
                            String nodeId = awDb.getOnlyKeyTargetId(Key_NodeFactory.EMAIL_ID, valueId, longTimestamp);
                            String line = valueId.substring(2);
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
