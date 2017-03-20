package com.jamador.gravity.screens;

/*
gdx
 */
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
gravity
 */
import com.jamador.gravity.GravityGame;

/**
 * Created by jason on 3/19/17.
 */
public class MainMenuScreen implements Screen, InputProcessor {
    final GravityGame game;

    private int screenWidth, screenHeight;

    private SpriteBatch batch;
    private Sprite ball;

    public MainMenuScreen(GravityGame game) {
        this.game = game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(this);
        ball = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        ball.setCenter(screenWidth / 2, screenHeight / 2);
        batch = new SpriteBatch();
    }

    /*
    Screen
     */
    @Override
    public void show() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        ball.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();
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
        if (ball.getBoundingRectangle().contains(screenX, screenY)) {
            game.setScreen(new GameScreen(game));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
