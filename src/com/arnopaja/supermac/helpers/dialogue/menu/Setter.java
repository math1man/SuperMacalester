package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.plot.Settings;

/**
* @author Ari Weiland
*/
public abstract class Setter<T> {

    public abstract void set(T value);

    public static final Setter<Integer> VOLUME = new Setter<Integer>() {
        @Override
        public void set(Integer value) {
            Settings.setVolume(value / 10.0f);
        }
    };

    public static final Setter<Boolean> CLEAN = new Setter<Boolean>() {
        @Override
        public void set(Boolean value) {
            Settings.setClean(value);
        }
    };
}
