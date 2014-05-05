package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public abstract class Menu extends DialogueOptions {
    protected Menu(DialogueMember... members) {
        super("menu", Arrays.asList(members), DialogueStyle.FULL_SCEEN);
    }

}
