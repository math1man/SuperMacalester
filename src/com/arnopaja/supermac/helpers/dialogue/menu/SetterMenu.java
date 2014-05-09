package com.arnopaja.supermac.helpers.dialogue.menu;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.dialogue.DialogueMember;
import com.arnopaja.supermac.helpers.interaction.Interaction;

import java.util.ArrayList;
import java.util.List;

/**
* @author Ari Weiland
*/
public class SetterMenu<T> extends Menu {

    public SetterMenu(String header, List<T> values, Setter<T> setter, Interaction interaction) {
        super(header, members(values, setter, interaction));
    }

    private static <T> DialogueMember[] members(List<T> values, final Setter<T> setter, final Interaction interaction) {
        DialogueMember[] members = new DialogueMember[values.size()];
        for (int i=0; i<values.size(); i++) {
            final T value = values.get(i);
            members[i] = new DialogueMember(value.toString(), new Interaction() {
                @Override
                public void run(GameScreen screen) {
                    setter.set(value);
                    interaction.run(screen);
                }
            });
        }
        return members;
    }

    public static List<Integer> getRange(int min, int max, int interval) {
        List<Integer> range = new ArrayList<Integer>();
        for (int i=min; i<=max; i = i + interval) {
            range.add(i);
        }
        return range;
    }
}
