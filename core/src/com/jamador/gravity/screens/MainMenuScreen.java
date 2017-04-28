package com.jamador.gravity.screens;

/*
gdx
 */
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private BitmapFont font;
    private SpriteBatch textRenderer;

    public MainMenuScreen(GravityGame game) {
        /*
        android buttons
         */
        this.game = game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        ball = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        ball.setCenter(screenWidth / 2, screenHeight / 2);
        batch = new SpriteBatch();

        /*
        text
         */
        font = new BitmapFont(Gdx.files.internal("font0.fnt"), Gdx.files.internal("font0.png"), false);
        textRenderer = new SpriteBatch();
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
        /*
        batch.begin();
        ball.draw(batch);
        batch.end();
        */

        /*
        text
         */
        textRenderer.begin();
        GlyphLayout layout = new GlyphLayout(font, "GRAVITY");
        font.draw(textRenderer, "GRAVITY", (screenWidth / 2) - (layout.width / 2), (screenHeight / 2) - (layout.height / 2));
        textRenderer.end();
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
