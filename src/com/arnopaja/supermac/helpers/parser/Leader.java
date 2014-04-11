package com.arnopaja.supermac.helpers.parser;

import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public enum Leader {
    DIALOGUE("dialogue"),
    DIALOGUE_OPTIONS("options"),
    MAP("map"),
    FLOOR_COUNT("floors"),
    FIRST_FLOOR_INDEX("first_floor_index");

    public final String leader;

    Leader(String leader) {
        this.leader = leader;
    }

    public boolean matches(String line) {
        return Pattern.matches("<" + leader + ":.*" + ">", line);
    }

    public String getParam(String line) {
        if (matches(line)) {
            return line.substring(leader.length()+2, line.length()-1).trim();
        }
        return null;
    }

    public boolean isParam(String line, String param) {
        return param.equals(getParam(line));
    }

    public String getEnd() {
        return "</" + leader + ">";
    }

    public boolean isEnd(String line) {
        return line.equals(getEnd());
    }
}
