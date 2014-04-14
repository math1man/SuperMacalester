package com.arnopaja.supermac.helpers;

import com.google.gson.JsonElement;

/**
 * @author Ari Weiland
 */
public interface Savable {

    JsonElement toJson();

    void fromJson(JsonElement element);

}
