package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.GameScreen;
import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.helpers.InteractionBuilder;
import com.arnopaja.supermac.helpers.SuperParser;
import com.arnopaja.supermac.helpers.dialogue.DialogueStyle;
import com.arnopaja.supermac.helpers.dialogue.DialogueText;
import com.arnopaja.supermac.inventory.Armor;
import com.arnopaja.supermac.inventory.Spell;
import com.arnopaja.supermac.inventory.Weapon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Nolan Varani
 */
public class Hero extends BattleCharacter implements InteractionBuilder {

    protected boolean hasFled = false;

    public Hero(String name, BattleClass battleClass) {
        this(name, battleClass, 1);
    }

    public Hero(String name, BattleClass battleClass, int level) {
        this(name, battleClass, level, -1, -1);
    }

    public Hero(String name, BattleClass battleClass, int level, int health, int mana) {
        super(name, battleClass, level, health, mana);
    }

    public void setHasFled(boolean a) {
        hasFled = a;
    }

    public boolean getHasFled() {
        return hasFled;
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
            int health = -1;
            if (object.has("health")) {
                health = getInt(object, "health");
            }
            int mana = -1;
            if (object.has("mana")) {
                mana = getInt(object, "mana");
            }
            Hero hero = new Hero(name, battleClass, level, health, mana);
            if (has(object, Armor.class)) {
                hero.setEquippedArmor(getObject(object, Armor.class));
            }
            if (has(object, Weapon.class)) {
                hero.setEquippedWeapon(getObject(object, Weapon.class));
            }
            if (object.has("spells")) {
                hero.addSpells(getList(object, "spells", Spell.class));
            }
            return hero;
        }

        @Override
        public JsonElement toJson(Hero object) {
            JsonObject json = new JsonObject();
            addString(json, "name", object.name);
            addObject(json, object.battleClass, BattleClass.class);
            addInt(json, "level", object.level);
            addFloat(json, "health", object.currentHealth);
            addFloat(json, "mana", object.currentMana);
            if (object.hasEquippedArmor()) {
                addObject(json, object.getEquippedArmor(), Armor.class);
            }
            if (object.hasEquippedWeapon()) {
                addObject(json, object.getEquippedWeapon(), Weapon.class);
            }
            addList(json, "spells", object.getSpellsList(), Spell.class);
            return json;
        }
    }
}
