package org.agilewiki.console.oodb.nodes.roles.admin;

import org.agilewiki.console.NameIds;
import org.agilewiki.console.RequestBlade;
import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.oodb.nodes.Key_Node;
import org.agilewiki.console.oodb.nodes.roles.Role;
import org.agilewiki.utils.ids.NameId;
import org.agilewiki.utils.ids.ValueId;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.PeekABoo;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;

/**
 * Change the email address of the user.
 */
public class EmailAddressesBlade extends RequestBlade {
    public EmailAddressesBlade(Role role, String page) throws Exception {
        super(role, page);
    }

    @Override
    public String niceName() {
        return "Email Addresses";
    }

    @Override
    public void get(String page, AsyncContext asyncContext, String userId, Role role) {
        new SR(page, asyncContext, userId, role) {
            @Override
            protected void process()
                    throws Exception {
                String prefix = SecondaryId.SECONDARY_ID + Key_Node.EMAIL_ID;
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
                        PeekABoo<String> idPeekABoo = db.idsIterable(prefix, longTimestamp).iterator();
                        idPeekABoo.setPosition(startingAt);
                        for (String id : idPeekABoo) {
                            if (limit == 0) {
                                hasMore = true;
                                startingAt = ValueId.value(id);
                                break;
                            }
                            --limit;
                            String secondaryId = SecondaryId.secondaryId(Key_Node.EMAIL_ID, id);
                            MapAccessor ma = db.mapAccessor();
                            ListAccessor la = ma.listAccessor(secondaryId);
                            String nodeId = null;
                            if (la != null) {
                                VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                                if (vmn != null) {
                                    nodeId = (String) vmn.firstKey(longTimestamp);
                                }
                            }
                            if (nodeId != null) {
                                sb.append("<a href=\"?from=secondaryKeys&to=user&nodeId=");
                                sb.append(nodeId);
                                if (timestamp != null) {
                                    sb.append("&timestamp=");
                                    sb.append(timestamp);
                                }
                                sb.append(setRole + "#rupa\">");
                                sb.append(ValueId.value(id));
                                sb.append("</a>");
                                sb.append("<br />");
                            }
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("emailAddresses", sb.toString());
                map.put("startingAt", hasMore ? SimpleSimon.encode(startingAt, 0, SimpleSimon.ENCODE_FIELD) : ""); //field
                finish();
            }
        }.signal();
    }
}
