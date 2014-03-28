package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.dialogue.Dialogue;

/**
 * Created by Envy on 2/28/14.
 */
public class Spell implements Usable {

    private final int universalID;
    private final String name;
    private final int damageModifier;

    public Spell(int universalID) {
        this.universalID = universalID;
        //Generate given its universalID
        this.name = null;
        this.damageModifier = 0;
    }

    @Override
    public Dialogue use(BattleCharacter source, BattleCharacter destination) {
        float damage = getDamageModifier() / (destination.getSpecial() / 4) * source.getSpecial();
        int damageDone = (int) destination.modifyHealth(-damage);
        String dialogue = source + " casts " + this + "!\n" +
                damageDone + " damage done.";
        if (destination.isFainted()) {
            dialogue += "\n" + destination + " fell!";
        }
        // TODO: decrement mana
        if (source.isOutOfMana()) {
            dialogue += "<d>" + source + " is out of mana...";
        }
        return new Dialogue(dialogue);
    }

    public String getName() { return name; }
    public int getDamageModifier() { return damageModifier; }

    @Override
    public String toString() {
        return name;
    }
}
