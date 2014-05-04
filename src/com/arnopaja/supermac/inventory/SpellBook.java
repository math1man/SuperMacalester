package com.arnopaja.supermac.inventory;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class SpellBook implements Iterable<Spell> {

    private final Map<Integer, Spell> spellBook;

    public SpellBook() {
        this(new LinkedHashMap<Integer, Spell>());
    }

    public SpellBook(Collection<Spell> spells) {
        this(new LinkedHashMap<Integer, Spell>());
        addAll(spells);
    }

    public SpellBook(Map<Integer, Spell> spells) {
        spellBook = spells;
    }

    public void add(Spell spell) {
        spellBook.put(spell.getId(), spell);
    }

    public void addAll(Collection<Spell> spells) {
        for (Spell spell : spells) {
            add(spell);
        }
    }

    public Spell get(int id) {
        return spellBook.get(id);
    }

    public boolean contains(Spell spell) {
        return spellBook.containsValue(spell);
    }

    public Spell remove(int id) {
        return spellBook.remove(id);
    }

    public boolean remove(Spell spell) {
        return spellBook.remove(spell.getId()) != null;
    }

    public int size() {
        return spellBook.size();
    }

    public List<Spell> asList() {
        return new ArrayList<Spell>(spellBook.values());
    }

    @Override
    public Iterator<Spell> iterator() {
        return asList().iterator();
    }
}
