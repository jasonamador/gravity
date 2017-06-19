package com.jamador.gravity.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.jamador.gravity.GravityGame;

public class TestScreen implements Screen, InputProcessor {
    GravityGame game;
    TextureAtlas atlas;
    float radius = 2;
    Array<Sprite> spriteArray;
    int frame = 0;
    boolean grow;

    /*
    SpriteBatch
     */
    SpriteBatch batch;
    OrthographicCamera camera;

    public TestScreen(final GravityGame game) {
        this.game = game;

        /*
        spritebatch
         */
        batch = new SpriteBatch();
        camera = new OrthographicCamera(160f, 100f);
        camera.position.set(80, 50, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        atlas = new TextureAtlas(Gdx.files.internal("textures/ball.atlas"));
        spriteArray = atlas.createSprites();
    }

    /*
    Screen
     */
    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        System.out.println((int)(radius * 2));
        if ((int)(radius * 2) >= spriteArray.size - 1)
            grow = false;
        if ((int)(radius * 2) <= 0)
            grow = true;
        if (grow)
            radius += 0.5;
        else
            radius -= 0.5;

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*
        spritebatch
         */
        batch.begin();
        spriteArray.get((int)(radius * 2)).setSize(radius * 2, radius * 2);
        spriteArray.get((int)(radius * 2)).setCenter(80, 50);
        spriteArray.get((int)(radius * 2)).draw(batch);
        spriteArray.get((int)(radius * 2)).draw(batch);
        batch.end();
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

