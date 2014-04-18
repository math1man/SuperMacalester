package com.arnopaja.supermac.helpers;

/**
 * Helper interface for Interactions. Describes one method,
 * toInteraction(), which converts the implementing object into
 * an interaction. Some static helper methods in Interaction
 * also use this as an input parameter. For convenience to
 * those methods, Interaction also implements this interface.
 *
 * @author Ari Weiland
 */
public interface InteractionBuilder {
    Interaction toInteraction();
}
