package com.arnopaja.supermac.helpers.parser;

import java.util.EnumMap;

/**
 * @author Ari Weiland
 */
public class ParsedObject {

    private final String name;
    private final Leader type;
    private final EnumMap<Leader, String> params;
    private final String[] data;

    protected ParsedObject(String name, Leader type, EnumMap<Leader, String> params, String[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public Leader getType() {
        return type;
    }

    public String[] getData() {
        return data;
    }

    public String getString(Leader key) {
        return params.get(key);
    }

    public int getInt(Leader key) {
        return Integer.parseInt(params.get(key));
    }

    public float getFloat(Leader key) {
        return Float.parseFloat(params.get(key));
    }

    public boolean getBoolean(Leader key) {
        return Boolean.parseBoolean(params.get(key));
    }
}
