package com.arnopaja.supermac.helpers;

import com.badlogic.gdx.audio.Music;

/**
 * @author Ari Weiland
 */
public class GameMode<T extends Controller> {
    private final Renderer<T> renderer;
    private T controller;

    public GameMode(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    public Renderer<T> getRenderer() {
        return renderer;
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
        renderer.setController(controller);
    }

    public Music getMusic() {
        return controller.getMusic();
    }
}
