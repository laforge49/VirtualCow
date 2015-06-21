package org.agilewiki.console.oodb.nodes.lnk1;

public class OfRole_Lnk1 extends Lnk1_NodeInstance {
    private static OfRole_Lnk1 ofRole_lnk1;

    public static OfRole_Lnk1 get() {
        return ofRole_lnk1;
    }

    public static void create() {
        ofRole_lnk1 = new OfRole_Lnk1("$nofRole.lnk1", "$nlnk1.node");
    }

    public OfRole_Lnk1(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
