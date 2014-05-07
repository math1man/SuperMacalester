package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.interaction.Interactions;

/**
* @author Ari Weiland
*/
public class PauseMenu extends Menu {

    public PauseMenu() {
        super("Pause Menu",
                // TODO: Party manipulation
                // TODO: Inventory manipulation
                new DialogueMember("Settings", new SettingsMenu()),
                new DialogueMember("Reset", new ConfirmationMenu("Are you sure you want to reset your game?",
                        Interactions.RESET, Interactions.END_DIALOGUE)),
                new DialogueMember("Exit", new ConfirmationMenu("Are you sure you want to exit?",
                        Interactions.CLOSE, Interactions.END_DIALOGUE)));
    }
}
