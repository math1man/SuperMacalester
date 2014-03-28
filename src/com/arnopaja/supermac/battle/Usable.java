package com.arnopaja.supermac.battle;

import com.arnopaja.supermac.helpers.dialogue.Dialogue;

/**
 * Created by Envy on 3/7/14.
 */
public interface Usable {

    public abstract Dialogue use(BattleCharacter source, BattleCharacter destination);

}
