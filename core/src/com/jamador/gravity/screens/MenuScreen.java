package com.jamador.gravity.screens;

/*
gdx
 */
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/*
gravity
 */
import com.jamador.gravity.GravityGame;

public class MenuScreen implements Screen, InputProcessor {
    private GravityGame game;

    /*
    Scene2D.UI
     */
    private Stage stage;

    public MenuScreen(final GravityGame game) {
        this.game = game;

        /*
        scene2d
         */
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        TextButton start = new TextButton("Start", skin, "text-only");
        TextButton settings = new TextButton("Settings", skin, "text-only");
        TextButton quit = new TextButton("Quit", skin, "text-only");
        final Window settingsWindow = new Window("Settings", skin);
        final CheckBox sound = new CheckBox("Sound", skin, "sound");
        settingsWindow.setFillParent(true);
        settingsWindow.add(sound);
        start.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor a) {
                game.setScreen(game.gameScreen);
            }
        });
        settings.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor a) {
                stage.addActor(settingsWindow);
            }
        });
        quit.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor a) {
                Gdx.app.exit();
            }
        });
        sound.addListener(new ChangeListener() {
            public void changed(ChangeEvent e, Actor a) {
                game.soundOn = sound.isChecked();
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.add(start).fillX().space(10);
        table.row();
        table.add(settings).fillX().space(10);
        table.row();
        table.add(quit).fillX().space(10);
        stage.addActor(table);

    }

    /*
    Screen
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
        scene2d
         */
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
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
        if (keycode == Input.Keys.T) {
            game.setScreen(game.testScreen);
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
        game.setScreen(game.gameScreen);

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
