package org.agilewiki.console.oodb.nodes.lnk1;

public class Destination_Lnk1 extends Lnk1_NodeInstance {
    private static Destination_Lnk1 destination_lnk1;

    public static Destination_Lnk1 get() {
        return destination_lnk1;
    }

    public static void create() {
        destination_lnk1 = new Destination_Lnk1("$ndestination.lnk1", "$nlnk1.node");
    }

    public Destination_Lnk1(String nodeId, String factoryId) {
        super(nodeId, factoryId);
    }
}
