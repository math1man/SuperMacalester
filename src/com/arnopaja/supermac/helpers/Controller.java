package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.audio.Music;

/**
 * @author Ari Weiland
 */
public abstract interface Controller {

    void update(float delta);
    Music getMusic();
}
