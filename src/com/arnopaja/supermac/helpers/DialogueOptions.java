package com.arnopaja.supermac.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class DialogueOptions {

    private final String rawOptions;
    private final List<String> options;
    private final int count;

    public DialogueOptions(String rawOptions) {
        this.rawOptions = rawOptions;
        String[] lines = rawOptions.split("[\n]");
        int beginOptionIndex = 0, endOptionIndex = lines.length;
        options = new ArrayList<String>(5);
        for (int i=0; i<lines.length; i++) {
            String line = lines[i].trim();
            if (Pattern.matches("<options:.*>", line)) {
                options.add(line.substring(9, line.length() - 1));
                beginOptionIndex = i;
            } else if (line.contains("</options>")) {
                endOptionIndex = i;
            } else if (i > beginOptionIndex && i < endOptionIndex && Pattern.matches("<option:.*>", line)) {
                String option = line.substring(8, line.length() - 1);
                options.add(option);
            }
        }
        count = options.size();
    }

    public String getHeader() {
        return options.get(0);
    }

    public String get(int option) {
        if (option <= count) {
            return options.get(option);
        } else {
            return "";
        }
    }

    public String getRawOptions() {
        return rawOptions;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCount() {
        return count;
    }
}
