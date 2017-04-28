package com.jamador.gravity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.jamador.gravity.GravityGame;
import com.jamador.gravity.models.GameWorld;
import com.jamador.gravity.renderers.GameRenderer;

import java.awt.event.KeyEvent;

/**
 * Created by jason on 3/19/17.
 */
public class GameScreen implements Screen, InputProcessor {
    private GravityGame game;
    private GameWorld gameWorld;
    private GameRenderer gameRenderer;


    public GameScreen(GravityGame game) {
        this.game = game;
        gameWorld = new GameWorld();
        gameRenderer = new GameRenderer(gameWorld);
        Gdx.input.setInputProcessor(this);
    }

    /*
    Screen
     */
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        //temp for debug draw
        gameRenderer.render();
        gameWorld.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /*
    InputProcessor
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == KeyEvent.VK_ESCAPE)
            gameWorld.gameOver();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gameRenderer.getCamera().unproject(gameWorld.mousePosition.set(screenX, screenY, 0));
        gameWorld.touchDown = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        gameRenderer.getCamera().unproject(gameWorld.mousePosition.set(screenX, screenY, 0));
        gameWorld.touchDown = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        gameRenderer.getCamera().unproject(gameWorld.mousePosition.set(screenX, screenY, 0));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        gameRenderer.getCamera().unproject(gameWorld.mousePosition.set(screenX, screenY, 0));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
