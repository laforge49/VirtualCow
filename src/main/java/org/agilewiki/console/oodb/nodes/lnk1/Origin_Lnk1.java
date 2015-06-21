package org.agilewiki.console.oodb.nodes.lnk1;

public class Origin_Lnk1 extends Lnk1_NodeInstance {
    private static Origin_Lnk1 origin_lnk1;

    public static void create() {
        origin_lnk1 = new Origin_Lnk1("$norigin.lnk1", "$nlnk1.node");
    }

    public Origin_Lnk1(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
