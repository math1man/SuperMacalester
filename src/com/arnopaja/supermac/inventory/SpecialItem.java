package com.arnopaja.supermac.inventory; /**
 * Created by Nolan on 2/16/14.
 */
public class SpecialItem {

    private final int universalID;
    private final String name;
    // TODO: image definition

    public SpecialItem(int universalID) {
        this.universalID = universalID;
        //Generate given its universalID
        this.name = null;
    }
}
