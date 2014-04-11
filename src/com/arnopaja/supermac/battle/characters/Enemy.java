package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.inventory.Item;

/**
 * Created by Envy on 2/28/14.
 */
public class Enemy extends BattleCharacter {

    private final Item myItem;
    private final boolean isBoss;

    public Enemy(String name, Item myItem, boolean isBoss) {
        super(name);
        this.myItem = myItem;
        this.isBoss = isBoss;
    }

    public Item getMyItem() {
        return myItem;
    }

    public boolean isBoss() {
        return isBoss;
    }
}
