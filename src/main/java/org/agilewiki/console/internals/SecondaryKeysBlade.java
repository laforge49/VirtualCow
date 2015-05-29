package org.agilewiki.console.internals;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class SecondaryKeysBlade extends RequestBlade {
    public SecondaryKeysBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    protected String niceName() {
        return "Secondary Keys";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {

            String secondaryType;
            String keyPrefix;

            @Override
            protected String setContext() {
                secondaryType = request.getParameter("secondaryType");
                keyPrefix = request.getParameter("keyPrefix");
                map.put("secondaryType", secondaryType);
                map.put("keyPrefix", keyPrefix);
                return "&secondaryType=" + secondaryType +
                        "&keyPrefix=" + keyPrefix;
            }

            @Override
            protected void process()
                    throws Exception {
                String prefix = SecondaryId.SECONDARY_ID + NameIds.generate(secondaryType);
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
                        for (String id : new IdIterable(servletContext, db, prefix, keyPrefix + startingAt, longTimestamp)) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = ValueId.value(id);
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
                            String line = id;
                            line = line.replaceAll("\r", "");
                            if (line.length() > 60)
                                line = line.substring(0, 60);
                            line = SimpleSimon.encode(line, 0, SimpleSimon.ENCODE_SINGLE_LINE); //line text
                            if (nodeId == null) {
                                sb.append("<a href=\"?from=secondaryKeys&to=nodes&secondaryId=");
                                sb.append(secondaryId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "\">");
                                sb.append(line);
                                sb.append("</a>");
                            } else {
                                sb.append(line);
                                sb.append(" -> ");
                                sb.append("<a href=\"?from=secondaryKeys&to=node&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "\">");
                                sb.append(nodeId);
                                sb.append("</a>");
                            }
                            sb.append("<br />");
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("secondaryKeys", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                map.put("secondaryType", secondaryType);
                map.put("keyPrefix", keyPrefix);
                finish();
            }
        }.signal();
    }
}
