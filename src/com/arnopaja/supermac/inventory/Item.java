package com.arnopaja.supermac.inventory;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;

/**
 * Created by Nolan on 2/16/14.
 */
public class Item extends AbstractItem {

    private final int equippableBitMask;
    // TODO: image definition

    public Item(byte universalID) {
        //Generate given its universalID
        super(universalID, null, 0);
        this.equippableBitMask = 0;
    }

    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        // TODO: use item
        String dialogue = source + " uses " + this + " on " + destination + "!";
        return new Dialogue(dialogue);
    }

    public int getEquippableBitMask() {
        return equippableBitMask;
    }

}
