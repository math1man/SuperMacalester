package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.helpers.interaction.Interactions;

import java.util.Arrays;

/**
 * @author Ari Weiland
 */
public class PauseMenu extends DialogueOptions {

    public static final PauseMenu INSTANCE = new PauseMenu();

    private PauseMenu() {
        super("PauseMenu", Arrays.asList(
                new DialogueMember("Menu"),
                // TODO: Party manipulation
                // TODO: Inventory manipulation
//                new DialogueMember("Settings"),
                new DialogueMember("Reset", RESET_DIALOGUE),
                new DialogueMember("Exit", EXIT_DIALOGUE)),
                DialogueStyle.FULL_SCEEN); // TODO: maybe make a new style?
    }

    private static final DialogueOptions RESET_DIALOGUE = new DialogueOptions(
            "Are you sure you want to reset your game?",
            Arrays.asList(
                    new DialogueMember("Yes", Interactions.RESET),
                    new DialogueMember("No", INSTANCE)
            ),
            DialogueStyle.FULL_SCEEN
    );

    private static final DialogueOptions EXIT_DIALOGUE = new DialogueOptions(
            "Are you sure you want to exit?",
            Arrays.asList(
                    new DialogueMember("Yes", Interactions.CLOSE),
                    new DialogueMember("No", INSTANCE)
            ),
            DialogueStyle.FULL_SCEEN
    );
}
