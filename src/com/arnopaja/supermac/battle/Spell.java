package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.battle.characters.BattleCharacter;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;

/**
 * Created by Envy on 2/28/14.
 */
public class Spell {

    private final int universalID;
    private final String name;
    private final int damageModifier;
    private final float manaCost;

    public Spell(int universalID) {
        this.universalID = universalID;
        //Generate given its universalID
        this.name = null;
        this.damageModifier = 0;
        this.manaCost = 0;
    }

    public DialogueText use(BattleCharacter source, BattleCharacter destination) {
        float damage = getDamageModifier() / (destination.getSpecial() / 4) * source.getSpecial();
        int damageDone = (int) destination.modifyHealth(-damage);
        String dialogue = source + " casts " + this + "!\n" +
                damageDone + " damage done.";
        if (destination.isFainted()) {
            dialogue += "\n" + destination + " fell!";
        }
        int mana = (int) source.modifyMana(manaCost);
        dialogue += "<d>" + source + " used " + mana + " mana.";
        if (source.isOutOfMana()) {
            dialogue += "\n" + source + " is out of mana...";
        }
        return new DialogueText(dialogue);
    }

    public String getName() {
        return name;
    }

    public int getDamageModifier() {
        return damageModifier;
    }

    public float getManaCost() {
        return manaCost;
    }

    @Override
    public String toString() {
        return name;
    }
}
