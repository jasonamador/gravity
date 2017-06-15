package com.jamador.gravity.screens;

/*
gdx
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
gravity
 */
import com.badlogic.gdx.math.Vector2;
import com.jamador.gravity.GravityGame;
import com.jamador.gravity.models.TextSprite;

public class TestScreen implements Screen, InputProcessor {
    private GravityGame game;

    private int screenWidth, screenHeight;

    private BitmapFont font100, font75, font50, font35, font25, font20;
    private SpriteBatch textRenderer;
    private SpriteBatch imageRenderer;
    private TextSprite textSprite;

    private double theta = 0;

    public TestScreen(GravityGame game) {
        this.game = game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        imageRenderer = new SpriteBatch();

        /*
        text
         */
        font20 = new BitmapFont(Gdx.files.internal("font5.fnt"), Gdx.files.internal("font5.png"), false);
        font25 = new BitmapFont(Gdx.files.internal("font4.fnt"), Gdx.files.internal("font4.png"), false);
        font35 = new BitmapFont(Gdx.files.internal("font3.fnt"), Gdx.files.internal("font3.png"), false);
        font50 = new BitmapFont(Gdx.files.internal("font2.fnt"), Gdx.files.internal("font2.png"), false);
        font75 = new BitmapFont(Gdx.files.internal("font1.fnt"), Gdx.files.internal("font1.png"), false);
        font100 = new BitmapFont(Gdx.files.internal("font0.fnt"), Gdx.files.internal("font0.png"), false);
        textRenderer = new SpriteBatch();

        /*
        text sprite
         */
        textSprite = new TextSprite("Testing", font100, new Vector2(screenWidth / 2, screenHeight / 2));
    }

    /*
    Screen
     */
    @Override
    public void show() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
        text
         */
        textRenderer.begin();
        textSprite.draw(textRenderer);
        textRenderer.end();
        theta += 0.04f;
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
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
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
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            game.setScreen(game.menuScreen);
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
        if (textSprite.contains(new Vector2(screenX, screenY))) {
            System.out.println("touched test");
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

