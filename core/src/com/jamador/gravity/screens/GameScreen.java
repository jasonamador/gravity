package com.jamador.gravity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jamador.gravity.GravityGame;
import com.jamador.gravity.models.Player;

/**
 * Created by jason on 3/19/17.
 */
public class GameScreen implements Screen, InputProcessor {
    final GravityGame game;

    private int screenWidth, screenHeight;

    private World world;
    private Player player;

    private SpriteBatch batch;

    public GameScreen(GravityGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        batch = new SpriteBatch();

        /*
        box2d
         */
        world = new World(new Vector2(0.0f, 0.0f), true);
        player = new Player(world, batch);
    }

    /*
    Screen
     */
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        player.render();
        batch.end();
        world.step(1/60f, 6, 2);
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
        /*
        get world coordinates
         */
        screenY = screenHeight - screenY;
        player.applyForce(new Vector2(screenX, screenY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.zeroForce();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        /*
        get world coordinates
         */
        screenY = screenHeight - screenY;
        player.applyForce(new Vector2(screenX, screenY));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
