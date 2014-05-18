package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.plot.Settings;

/**
* @author Ari Weiland
*/
public abstract class Adjuster<T> {

    public abstract void adjust(T value);

    public static final Adjuster<Integer> VOLUME = new Adjuster<Integer>() {
        @Override
        public void adjust(Integer value) {
            Settings.setVolume(value / 10.0f);
        }
    };

    public static final Adjuster<Boolean> CLEAN = new Adjuster<Boolean>() {
        @Override
        public void adjust(Boolean value) {
            Settings.setClean(value);
        }
    };
}
