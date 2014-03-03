package com.arnopaja.supermac.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class Dialogue {

    private String rawDialogue;
    private List<String> parsedDialogue;
    private int position;

    private boolean hasOptions = false;
    private List<String> options;

    public Dialogue(String rawDialogue) {
        this.rawDialogue = rawDialogue;
        parseDialogue();
    }

    public void parseDialogue() {
        // converts the basic dialogue string into a List of lines
        // and other functionality
        String[] lines = rawDialogue.split("\n");
        parsedDialogue = new ArrayList<String>(lines.length);
        int beginOptionIndex = 0, endOptionIndex = lines.length;
        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            if (line.contains("<options>")) {
                hasOptions = true;
                options = new ArrayList<String>(4);
                beginOptionIndex = i;
            } else if (line.contains("</options>")) {
                endOptionIndex = i;
            } else if (i > beginOptionIndex && i < endOptionIndex && Pattern.matches("<option:*>", line)) {
                String option = line.substring(8, line.length() - 1);
                options.add(option);
            } else {
                parsedDialogue.add(line);
            }
        }
    }

    public void next() {
        position++;
    }

    public String getRawDialogue() {
        return rawDialogue;
    }

    public boolean hasOptions() {
        return hasOptions;
    }

    public List<String> getOptions() {
        return options;
    }
}
