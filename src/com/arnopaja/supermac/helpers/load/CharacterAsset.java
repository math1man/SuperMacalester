package com.arnopaja.supermac.helpers.load;

import com.arnopaja.supermac.world.grid.Direction;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

/**
 * @author Ari Weiland
 */
public class CharacterAsset {
    private final EnumMap<Direction, TextureRegion> sprites;
    private final EnumMap<Direction, Animation> animations;

    public CharacterAsset(EnumMap<Direction, TextureRegion> sprites, EnumMap<Direction, Animation> animations) {
        this.sprites = sprites;
        this.animations = animations;
    }

    public TextureRegion getSprite(Direction direction) {
        return sprites.get(direction);
    }

    public Animation getAnimation(Direction direction) {
        return animations.get(direction);
    }
}
