package com.arnopaja.supermac.battle.characters;

import com.arnopaja.supermac.helpers.load.Parsable;
import com.arnopaja.supermac.helpers.load.SuperParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Nolan Varani
 */
public class Party<T extends BattleCharacter> implements Iterable<T>, Parsable {

    protected static final Random random = new Random();

    protected final List<T> characters;

    public Party() {
        this(new ArrayList<T>());
    }

    public Party(List<T> characters) {
        this.characters = new ArrayList<T>(characters);
    }
    //Characters will be added to party in different methods, as defined by their non-abstract subclasses

    public boolean isDefeated() {
        for (T character : characters) {
            if (!character.isFainted()) return false;
        }
        return true;
    }

    public int getIndex(BattleCharacter b)
    {
        return this.characters.indexOf(b);
    }

    public T get(int index) {
        return characters.get(index);
    }

    /**
     * Returns a random member of the active party
     * @return
     */
    public T getRandom() {
        List<T> charlist = getActiveParty();
        int i = random.nextInt(charlist.size());
        return charlist.get(i);
    }

    public int size() {
        return characters.size();
    }

    /**
     * Returns a list of all members of the battle party that are not fainted.
     * @return
     */
    public List<T> getActiveParty() {
        List<T> active = new ArrayList<T>();
        for(T character : getBattleParty()) {
            if(!character.isFainted()){
                active.add(character);
            }
        }
        return active;
    }

    /**
     * Returns up to the first four members of the party.
     * @return
     */
    public List<T> getBattleParty() {
        List<T> battle = new ArrayList<T>();
        for(int i=0; i<Math.min(4, characters.size()); i++) {
            battle.add(characters.get(i));
        }
        return battle;
    }

    public void clearDefend() {
        for (T bc : characters) {
            bc.clearDefend();
        }
    }

    public void clearPowerup(){
        for (T bc : characters){
            bc.clearPowerup();
        }
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        for (T bc : getBattleParty()) {
            sb.append(bc);
            if (bc.isFainted()) {
                sb.append(": Fainted!\n");
            } else {
                sb.append(": HP: ").append(bc.getHealth())
                        .append(", MP: ").append(bc.getMana()).append("\n");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return characters.iterator();
    }

    public boolean hasFled() {
        for (T bc : characters) {
            if (bc.hasFled()) return true;
        }
        return false;
    }

    public void clearHasFled() {
        for (T bc : characters) {
            bc.clearFlee();
        }
    }

    public void addCharacter(T bc) {
        characters.add(bc);
    }

    public void swapCharacter(int index1, int index2) {
        T temp;
        temp = characters.get(index1);
        characters.set(index1, characters.get(index2));
        characters.set(index2, temp);
    }

    public void restoreAll() {
        for (T bc : characters) {
            bc.fullRestore();
        }
    }

    public static class Parser extends SuperParser<Party> {
        @Override
        public Party fromJson(JsonElement element) {
            JsonObject object = element.getAsJsonObject();
            return new Party(getList(object, "characters", BattleCharacter.class));
        }

        @Override
        public JsonElement toJson(Party object) {
            JsonObject json = new JsonObject();
            addList(json, "characters", object.characters, BattleCharacter.class);
            return json;
        }
    }
}
