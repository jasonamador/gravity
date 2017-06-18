package com.jamador.gravity.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jamador.gravity.GravityGame;

public class TestScreen implements Screen, InputProcessor {
    private GravityGame game;
    private InputMultiplexer inputMultiplexer;

    private int screenWidth, screenHeight;

    /*
    Scene2D.UI
     */
    Skin skin;
    Stage stage;

    /*
    SpriteBatch
     */
    SpriteBatch batch;
    OrthographicCamera camera;
    Sprite ball;


    public TestScreen(GravityGame game) {
        this.game = game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();


        /*
        spritebatch
         */
        batch = new SpriteBatch();
        camera = new OrthographicCamera(160f, 100f);
        camera.position.set(80, 50, 0);
        camera.update();
        ball = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        ball.setSize(5, 5);
        ball.setCenter(80, 50);

        /*
        scene2d
         */
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton start = new TextButton("Start", skin, "default");
        start.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor a) {
                System.out.println("Clicked");
            }
        });
        stage.addActor(start);

        inputMultiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /*
    Screen
     */
    @Override
    public void show() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*
        spritebatch
         */
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        ball.draw(batch);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        Gdx.input.setInputProcessor(inputMultiplexer);
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
        System.out.println(screenX + " " + screenY);
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

