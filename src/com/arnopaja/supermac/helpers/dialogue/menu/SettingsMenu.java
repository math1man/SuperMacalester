package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.plot.Settings;

import java.util.Arrays;

/**
* @author Ari Weiland
*/
public class SettingsMenu extends Menu {
    public SettingsMenu() {
        super(new DialogueMember("Settings"),
                new DialogueMember("Volume: " + (int) (Settings.getVolume() * 10), new SetterMenu<Integer>(
                        "Set Volume:", SetterMenu.getRange(0, 10, 1), Setter.VOLUME, new SettingsMenu())),
                new DialogueMember("Clean Dialogue: " + Settings.isClean(), new SetterMenu<Boolean>(
                        "Set Clean Dialogue:", Arrays.asList(true, false), Setter.CLEAN, new SettingsMenu()))
        );
    }
}
