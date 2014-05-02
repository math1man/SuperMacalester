package com.arnopaja.supermac.helpers;

import com.arnopaja.supermac.inventory.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nolan Varani
 */
public class EffectParser {

    public static List<Effect> parse(String parseString) {
        List<Effect> effectsList = new ArrayList<Effect>();
        if (parseString != null && !parseString.isEmpty()) {
            String[] parseStrings = parseString.split(",");
            for (String s : parseStrings) {
                Effect.Type type = Effect.Type.get(s.substring(0, 1));
                // upper case => self
                // lower case => other
                boolean self = Character.isUpperCase(s.charAt(0));
                int value = Integer.parseInt(s.substring(1));
                if (type != null) {
                    effectsList.add(new Effect(type, self, value));
                }
            }
        }
        return effectsList;
    }

    public static String toString(List<Effect> effects) {
        StringBuilder sb = new StringBuilder();
        for (Effect effect : effects) {
            sb.append(effect).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
