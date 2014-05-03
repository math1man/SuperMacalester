package com.arnopaja.supermac.helpers.dialogue;

import com.arnopaja.supermac.GameScreen;

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
                new DialogueMember("Reset", RESET_DIALOGUE.toInteraction()),
                new DialogueMember("Exit", EXIT_DIALOGUE.toInteraction())),
                DialogueStyle.FULL_SCEEN); // TODO: maybe make a new style?
    }

    private static final DialogueOptions RESET_DIALOGUE = new DialogueOptions(
            "Are you sure you want to reset your game?",
            Arrays.asList(
                    new DialogueMember("Yes", GameScreen.RESET),
                    new DialogueMember("No", INSTANCE.toInteraction())
            ),
            DialogueStyle.FULL_SCEEN
    );

    private static final DialogueOptions EXIT_DIALOGUE = new DialogueOptions(
            "Are you sure you want to exit?",
            Arrays.asList(
                    new DialogueMember("Yes", GameScreen.CLOSE),
                    new DialogueMember("No", INSTANCE.toInteraction())
            ),
            DialogueStyle.FULL_SCEEN
    );
}
