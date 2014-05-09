package com.arnopaja.supermac.helpers.interaction;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.load.AssetLoader;
import com.arnopaja.supermac.plot.Settings;
import com.badlogic.gdx.Gdx;

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
    SAVE {
        @Override
        public void run(GameScreen screen) {
            screen.save();
        }
    },
    LOAD {
        @Override
        public void run(GameScreen screen) {
            screen.load();
        }
    },
    RESET {
        @Override
        public void run(GameScreen screen) {
            AssetLoader.prefs.clear();
            Settings.save(true); // resave Settings
            screen.load(); // will load the default
            END_DIALOGUE.run(screen);
        }
    },
    EXIT {
        @Override
        public void run(GameScreen screen) {
            Gdx.app.exit();
        }
    },
    HEAL {
        @Override
        public void run(GameScreen screen) {
            screen.getParty().restoreAll();
        }
    }
}
