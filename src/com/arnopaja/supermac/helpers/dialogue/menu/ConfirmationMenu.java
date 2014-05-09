package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.interaction.Interaction;

/**
* @author Ari Weiland
*/
public class ConfirmationMenu extends Menu {
    public ConfirmationMenu(String header, Interaction confirmed, Interaction rejected) {
        super(header,
                new DialogueMember("Yes", confirmed),
                new DialogueMember("No", rejected));
    }
}
