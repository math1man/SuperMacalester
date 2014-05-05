package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.interaction.Interaction;

/**
* @author Ari Weiland
*/
public class ConfirmationMenu extends Menu {
    public ConfirmationMenu(String header, Interaction confirmed) {
        super(new DialogueMember(header),
                new DialogueMember("Yes", confirmed),
                new DialogueMember("No", new PauseMenu()));
    }
}
