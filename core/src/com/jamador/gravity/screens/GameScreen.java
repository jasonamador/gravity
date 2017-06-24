package com.jamador.gravity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.jamador.gravity.GravityGame;
import com.jamador.gravity.models.GameWorld;
import com.jamador.gravity.renderers.GameRenderer;

public class GameScreen implements Screen, InputProcessor {
    private GravityGame game;
    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private Sound drums;

    public GameScreen(GravityGame game) {
        this.game = game;
        gameWorld = new GameWorld(this);
        gameRenderer = new GameRenderer(gameWorld);
        drums = Gdx.audio.newSound(Gdx.files.internal("drums.wav"));
        drums.loop();
    }

    public void reset() {
        gameRenderer.reset();
    }

    /*
    Screen
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
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
        Gdx.input.setInputProcessor(this);
        drums.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        drums.dispose();
    }

    /*
    InputProcessor
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            gameWorld.gameOver();
            game.setScreen(game.menuScreen);
        }
        if (keycode == Input.Keys.PLUS) {
            gameWorld.increaseTimestep();
        }
        if (keycode == Input.Keys.MINUS) {
            gameWorld.decreaseTimestep();
        }
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
