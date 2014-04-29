package com.arnopaja.supermac.plot;

import com.arnopaja.supermac.helpers.Interaction;
import com.arnopaja.supermac.world.objects.EntityInterface;

/**
 * @author Ari Weiland
 */
public interface QuestEntity extends EntityInterface{
    void activate(Interaction interaction);
    void deactivate(boolean delay);
}
