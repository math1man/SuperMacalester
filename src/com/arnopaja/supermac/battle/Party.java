package com.arnopaja.supermac.battle;

import java.util.List;

/**
 * TODO should this maybe implement a Collection?
 * Created by Nolan on 2/24/14.
 */
public abstract class Party<T extends BattleCharacter> {

    protected final List<T> characters;

    protected Party(List<T> characters) {
        this.characters = characters;
    }
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses

    public boolean isDefeated() {
        for (T character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }

    public BattleCharacter get(int index) {
        return characters.get(index);
    }

    public BattleCharacter getRandom() {
        // TODO: ???
        return null;
    }

    public BattleCharacter[] getBattleParty() {
        BattleCharacter battleParty[] = new BattleCharacter[3];
        for(int i=0;i<3;i++) battleParty[i] = characters.get(i);
        return battleParty;
    }

    public int getSize() {
        return characters.size();
    }
}
