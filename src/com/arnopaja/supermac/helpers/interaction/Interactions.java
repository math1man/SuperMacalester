package com.arnopaja.supermac.helpers.interaction;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.plot.Settings;

/**
 * @author Ari Weiland
 */
public enum Interactions implements Interaction {
    NULL {
        @Override
        public void run(GameScreen screen) {}
    },
    END_DIALOGUE() {
        @Override
        public void run(GameScreen screen) {
            screen.endDialogue();
        }
    },
    RESET {
        @Override
        public void run(GameScreen screen) {
            AssetLoader.prefs.clear();
            Settings.save(true); // resave Settings
            screen.load(); // will load the default
        }
    },
    CLOSE {
        @Override
        public void run(GameScreen screen) {
            screen.getGame().dispose();
        }
    }
}
