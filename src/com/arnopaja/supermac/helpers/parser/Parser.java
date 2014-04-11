package com.arnopaja.supermac.helpers.parser;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.dialogue.Dialogue;
import com.arnopaja.supermac.helpers.dialogue.DialogueDisplayable;
import com.arnopaja.supermac.helpers.dialogue.DialogueOptions;
import com.badlogic.gdx.files.FileHandle;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * TODO: Parse Entities
 * TODO: Parse Locations
 * TODO: Parse Interactions
 * TODO: Parse Goals and Quests
 * @author Ari Weiland
 */
public class Parser {

    /**
     * Parses an object of the specified name and type specified by leader in
     * the specified file. If no object of the given name and leader is found,
     * this method returns null.
     *
     * @param name
     * @param leader
     * @param handle
     * @return
     */
    public static ParsedObject parse(String name, Leader leader, FileHandle handle) {
        String raw = handle.readString();
        String[] lines = raw.split("\n");
        int[] indexes = getIndexes(name, leader, lines);
        if (indexes[1] == -1) {
            return null;
        }
        int start = indexes[0] + 1;
        int end = indexes[1];
        EnumMap<Leader, String> params = new EnumMap<Leader, String>(Leader.class);
        for (int i=start; i<end; i++) {
            String line = lines[i];
            for (Leader l : Leader.values()) {
                if (l.matches(line)) {
                    params.put(l, l.getParam(line));
                    start++;
                }
            }
        }
        return new ParsedObject(name, leader, params, Arrays.copyOfRange(lines, start, end));
    }

    public static DialogueDisplayable parseDialogue(String name, FileHandle handle) {
        return parseDialogue(name, handle, new Interaction[0]);
    }

    /**
     * Parses a dialogue instance of the specified name from the specified file.
     * It will parse either a dialogue, or dialogue options, and if both exist,
     * it will parse the dialogue with the options at the end. For only dialogue,
     * the first interaction (if any) will be given as a the post interaction.
     * Otherwise, the interactions will be applied to the dialogue options.
     * If the method does not find any dialogue or options, it returns null.
     *
     * @param name
     * @param handle
     * @param interactions
     * @return
     */
    public static DialogueDisplayable parseDialogue(String name, FileHandle handle, Interaction... interactions) {
        ParsedObject dialogue = parse(name, Leader.DIALOGUE, handle);
        ParsedObject options = parse(name, Leader.DIALOGUE_OPTIONS, handle);
        if (dialogue == null && options == null) {
            return null;
        } else if (dialogue == null) {
            return new DialogueOptions(options.getData(), interactions);
        } else if (options == null) {
            if (interactions.length > 0) {
                return new Dialogue(merge(dialogue.getData()), interactions[0]);
            } else {
                return new Dialogue(merge(dialogue.getData()));
            }
        } else {
            return new Dialogue(merge(dialogue.getData()), new DialogueOptions(options.getData(), interactions));
        }
    }

    private static int[] getIndexes(String name, Leader leader, String[] lines) {
        int[] indexes = {-1, -1};
        for (int i=0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (indexes[0] == -1) {
                if (leader.isParam(line, name)) {
                    indexes[0] = i;
                }
            } else if (leader.isEnd(line)) {
                indexes[1] = i;
                return indexes;
            }
        }
        return indexes;
    }

    private static String merge(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }
}
