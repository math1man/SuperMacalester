package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.audio.Music;

/**
 * @author Ari Weiland
 */
public abstract interface Controller extends Parsable {

    void update(float delta);
    Music getMusic();
}
