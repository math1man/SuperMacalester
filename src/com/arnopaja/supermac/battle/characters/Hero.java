package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.world.objects.MainMapCharacter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter implements InteractionBuilder {

    private final MainMapCharacter main;
    protected boolean hasFled = false;

    public Hero(String name, BattleClass battleClass) {
        this(name, battleClass, 1);
    }

    public Hero(String name, BattleClass battleClass, int level) {
        this(name, battleClass, level, 1, 1);
    }

    public Hero(String name, BattleClass battleClass, int level, int fractionHealth, int fractionMana) {
        this(name, battleClass, level, fractionHealth, fractionMana, null);
    }

    public Hero(String name, BattleClass battleClass, int level, int fractionHealth, int fractionMana, MainMapCharacter character) {
        super(name, battleClass, level, fractionHealth, fractionMana);
        main = character;
    }

    public void setHasFled(boolean a)
    {
        hasFled = a;
    }
    public boolean getHasFled()
    {
        return hasFled;
    }

    @Override
    public Interaction toInteraction() {
        if (main == null) {
            return Interaction.NULL;
        } else {
            final Hero hero = this;
            return new Interaction(hero) {
                @Override
                public void run(GameScreen screen) {
                    main.addToParty(hero);
                    new DialogueText(hero.name + " has joined the party!", DialogueStyle.WORLD).toInteraction().run(screen);
                }
            };
        }
    }

    public static class Parser extends SuperParser<Hero> {
        @Override
        public Hero fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            String name = getString(object, "name");
            BattleClass battleClass = getObject(object, BattleClass.class);
            int level = getInt(object, "level");
            //TODO fix int here
            int health = 1;
            if (object.has("health")) {
                health = getInt(object, "health");
            }
            //TODO fix int here
            int mana = 1;
            if (object.has("mana")) {
                mana = getInt(object, "mana");
            }
            return new Hero(name, battleClass, level, health, mana, world.getMainCharacter());
        }

        @Override
        public JsonElement toJson(Hero object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.battleClass, BattleClass.class);
            addInt(json, "level", object.level);
            addFloat(json, "health", object.currentHealth);
            addFloat(json, "mana", object.currentMana);
            return json;
        }
    }
}
