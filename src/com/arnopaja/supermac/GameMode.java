package com.arnopaja.supermac;

import com.arnopaja.supermac.helpers.Controller;
import com.arnopaja.supermac.helpers.InputHandler;
import com.arnopaja.supermac.helpers.Renderer;
import com.badlogic.gdx.audio.Music;

/**
* @author Ari Weiland
*/
public class GameMode<T extends Controller> {
    private final Renderer<T> renderer;
    private final InputHandler inputHandler;
    private T controller;

    public GameMode(Renderer<T> renderer, InputHandler inputHandler) {
        this.renderer = renderer;
        this.inputHandler = inputHandler;
    }

    public Renderer<T> getRenderer() {
        return renderer;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
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
