package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter implements InteractionBuilder {

    public Hero(String name, BattleClass battleClass) {
        this(name, battleClass, 1);
    }

    public Hero(String name, BattleClass battleClass, int level) {
        this(name, battleClass, level, 1, 1);
    }

    public Hero(String name, BattleClass battleClass, int level, float fractionHealth, float fractionMana) {
        super(name, battleClass, level, fractionHealth, fractionMana);
    }

    @Override
    public Interaction toInteraction() {
        final Hero hero = this;
        return new Interaction(hero) {
            @Override
            public void run(GameScreen screen) {
                screen.getParty().addCharacter(hero);
                new DialogueText(hero.name + " has joined the party!", DialogueStyle.WORLD).toInteraction().run(screen);
            }
        };
    }

    public static class Parser extends SuperParser<Hero> {
        @Override
        public Hero fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            BattleClass battleClass = getObject(object, BattleClass.class);
            int level = getInt(object, "level");
            float health = 1;
            if (object.has("health")) {
                health = getFloat(object, "health");
            }
            float mana = 1;
            if (object.has("mana")) {
                mana = getFloat(object, "mana");
            }
            return new Hero(name, battleClass, level, health, mana);
        }

        @Override
        public JsonElement toJson(Hero object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.battleClass, BattleClass.class);
            addInt(json, "level", object.level);
            addFloat(json, "health", object.fractionHealth);
            addFloat(json, "mana", object.fractionMana);
            return json;
        }
    }
}
