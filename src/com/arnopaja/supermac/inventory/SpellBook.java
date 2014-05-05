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

    public boolean add(Spell spell) {
        return spellBook.put(spell.getId(), spell) != spell;
    }

    public boolean addAll(Collection<? extends Spell> spells) {
        boolean changed = false;
        for (Spell spell : spells) {
            changed = add(spell) || changed;
        }
        return changed;
    }

    public Spell get(int id) {
        return spellBook.get(id);
    }

    public boolean contains(Object spell) {
        return spell instanceof Spell && spellBook.containsValue(spell);
    }

    public Spell remove(int id) {
        return spellBook.remove(id);
    }

    public boolean remove(Object spell) {
        return spell instanceof Spell && remove(((Spell) spell).getId()) != null;
    }

    public void clear() {
        spellBook.clear();
    }

    public int size() {
        return spellBook.size();
    }

    public boolean isEmpty() {
        return spellBook.isEmpty();
    }

    public List<Spell> asList() {
        return Collections.unmodifiableList(new ArrayList<Spell>(spellBook.values()));
    }

    @Override
    public Iterator<Spell> iterator() {
        return asList().iterator();
    }
}
