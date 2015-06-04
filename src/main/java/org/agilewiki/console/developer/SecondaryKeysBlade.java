package org.agilewiki.console.developer;

import org.agilewiki.console.*;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.*;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Request for secondary keys.
 */
public class SecondaryKeysBlade extends RequestBlade {

    String secondaryType;
    String keyPrefix;
    String niceName;

    public SecondaryKeysBlade(SimpleSimon simpleSimon, String niceName, String secondaryType, String keyPrefix)
            throws Exception {
        super(simpleSimon);
        this.niceName = niceName;
        this.secondaryType = secondaryType;
        this.keyPrefix = keyPrefix;
    }

    @Override
    protected String niceName() {
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
                if (startingAt == null)
                    startingAt = "";
                boolean hasMore = false;
                StringBuilder sb;
                while (true) {
                    try {
                        hasMore = false;
                        int limit = 25;
                        sb = new StringBuilder();
                        PeekABoo<String> idPeekABoo = new IdPeekABooable(db, prefix, longTimestamp).iterator();
                        idPeekABoo.setPosition(keyPrefix + startingAt);
                        for (String id: idPeekABoo) {
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
                map.put("secondaryType", secondaryType);
                map.put("keyPrefix", keyPrefix);
                finish();
            }
        }.signal();
    }
}
