package org.agilewiki.console.requests;

import org.agilewiki.console.SimpleSimon;
import org.agilewiki.console.TimestampIds;
import org.agilewiki.utils.ids.composites.Journal;
import org.agilewiki.utils.ids.composites.Link1Id;
import org.agilewiki.utils.ids.composites.Link2Id;
import org.agilewiki.utils.ids.composites.SecondaryId;
import org.agilewiki.utils.immutable.FactoryRegistry;
import org.agilewiki.utils.immutable.collections.ListAccessor;
import org.agilewiki.utils.immutable.collections.MapAccessor;
import org.agilewiki.utils.immutable.collections.VersionedMapNode;
import org.agilewiki.utils.virtualcow.UnexpectedChecksumException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Request for a journal entry.
 */
public class NodeBlade extends RequestBlade {
    public NodeBlade(SimpleSimon simpleSimon) throws Exception {
        super(simpleSimon);
    }

    @Override
    public void get(String page, AsyncContext asyncContext) {
        new SR(page, asyncContext) {
            @Override
            protected void process()
                    throws Exception {
                String timestamp = request.getParameter("timestamp");
                String dateInString = request.getParameter("date");
                if (dateInString != null && dateInString.length() > 0) {
                    Date date;
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    try {
                        date = formatter.parse(dateInString);
                    } catch (ParseException e) {
                        throw new ServletException(e);
                    }
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.set(Calendar.SECOND, 59);
                    long time = calendar.getTimeInMillis() + 999;
                    timestamp = TimestampIds.value(TimestampIds.timestampId((time << 10) + 1023));
                }
                long longTimestamp;
                if (timestamp != null) {
                    map.put("setTimestamp", "&timestamp=" + timestamp);
                    map.put("atTime", "at " + SimpleSimon.niceTime(TimestampIds.generate(timestamp)));
                    longTimestamp = TimestampIds.timestamp(TimestampIds.generate(timestamp));
                } else {
                    longTimestamp = FactoryRegistry.MAX_TIMESTAMP;
                    map.put("post", "post");
                }
                String nodeId = request.getParameter("nodeId");
                String time = null;
                String jeTimestamp = "";
                if (nodeId.startsWith("$t")) {
                    jeTimestamp = TimestampIds.value(nodeId);
                    time = SimpleSimon.niceTime(nodeId);
                }
                StringBuilder sb;
                while (true) {
                    try {
                        sb = new StringBuilder();
                        MapAccessor ma = db.mapAccessor();

                        ListAccessor la = ma.listAccessor(nodeId);
                        if (la != null) {
                            VersionedMapNode vmn = (VersionedMapNode) la.get(0);
                            if (vmn != null && !vmn.isEmpty(longTimestamp)) {
                                sb.append("node id: ");
                                sb.append(nodeId);
                                if (time != null) {
                                    sb.append(" (");
                                    sb.append(time);
                                    sb.append(")");
                                }
                                sb.append("<br />");
                                MapAccessor vma = vmn.mapAccessor(longTimestamp);
                                for (ListAccessor vla : vma) {
                                    int sz = vla.size();
                                    for (int i = 0; i < sz; ++i) {
                                        String s = vla.key() + "[" + i + "] = ";
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + s +
                                                SimpleSimon.encode("" + vla.get(i), s.length() + 4,
                                                        SimpleSimon.ENCODE_MULTIPLE_LINES)); //body text
                                        sb.append("<br />");
                                    }
                                }
                                if (time != null) {
                                    sb.append("Modifies: <br />");
                                    for (String nId : db.keysIterable(Journal.modifiesId(nodeId), longTimestamp)) {
                                        if (nId.startsWith(SecondaryId.SECONDARY_ID))
                                            continue;
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;" + nId + "<br />");
                                        if (nId.startsWith(SecondaryId.SECONDARY_INV)) {
                                            VersionedMapNode icvmn = db.get(nId);
                                            if (icvmn != null) {
                                                MapAccessor icma = icvmn.mapAccessor(longTimestamp);
                                                for (ListAccessor icla : icma) {
                                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                                                    sb.append(icla.key());
                                                    sb.append("<br />");
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    sb.append("Modified by: <br />");
                                    for (String jeId : db.keysIterable(Journal.journalId(nodeId), longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"?from=node&to=node&nodeId=");
                                        sb.append(jeId);
                                        if (timestamp != null) {
                                            sb.append("&timestamp=" + timestamp);
                                        }
                                        sb.append("\">");
                                        sb.append(jeId);
                                        sb.append("</a> (" + SimpleSimon.niceTime(jeId) + ")<br />");
                                    }
                                }
                                sb.append("Secondary Keys: <br />");
                                for (String typeId : SecondaryId.typeIdIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: " + typeId + "<br />");
                                    for (String secondaryId :
                                            SecondaryId.secondaryIdIterable(db, nodeId, typeId, longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;secondaryId - " +
                                                secondaryId + "<br />");
                                    }
                                }
                                sb.append("Links: <br />");
                                for (String typeId: Link1Id.link1LabelIdIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: " + typeId + "<br />");
                                    for (String targetId: Link1Id.link1IdIterable(db, nodeId, typeId, longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + targetId + "<br />");
                                    }
                                }
                                sb.append("Inverted Links: <br />");
                                for (String typeId: Link1Id.link1LabelInvIterable(db, nodeId)) {
                                    sb.append("&nbsp;&nbsp;&nbsp;&nbsp;typeId: " + typeId + "<br />");
                                    for (String originId: Link1Id.link1InvIterable(db, nodeId, typeId, longTimestamp)) {
                                        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + originId + "<br />");
                                    }
                                }
                            }
                        }
                        break;
                    } catch (UnexpectedChecksumException uce) {
                    }
                }
                map.put("node", sb.toString());
                map.put("nodeId", nodeId);
                if (time != null &&
                        (timestamp == null || !timestamp.equals(jeTimestamp))) {
                    map.put("jeTimestamp", jeTimestamp);
                    map.put("time", "go to " + time);
                }
                if (timestamp != null) {
                    map.put("clearTime", "<a href=\"?from=node&to=node&nodeId=" + nodeId + "\">Clear selected time</a>");
                }
                finish();
            }
        }.signal();
    }
}
