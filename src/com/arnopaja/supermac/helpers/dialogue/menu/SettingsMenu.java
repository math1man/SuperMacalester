package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.interaction.Interactions;
import com.arnopaja.supermac.helpers.interaction.MultiInteraction;
import com.arnopaja.supermac.plot.Settings;

import java.util.Arrays;

/**
* @author Ari Weiland
*/
public class SettingsMenu extends Menu {

    public SettingsMenu() {
        super("Settings",
                new DialogueMember("Volume: " + (int) (Settings.getVolume() * 10), new SetterMenu<Integer>(
                        "Set Volume:", SetterMenu.getRange(0, 10, 1), Adjuster.VOLUME, Interactions.END_DIALOGUE)),
                new DialogueMember("Clean Dialogue: " + Settings.isClean(), new SetterMenu<Boolean>(
                        "Set Clean Dialogue:", Arrays.asList(true, false), Adjuster.CLEAN,
                        new MultiInteraction(Interactions.SAVE, Interactions.LOAD, Interactions.END_DIALOGUE))),
                new DialogueMember("Close", Interactions.END_DIALOGUE)
        );
    }
}
