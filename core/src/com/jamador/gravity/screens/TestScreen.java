package com.jamador.gravity.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LongArray;
import com.jamador.gravity.GravityGame;

public class TestScreen implements Screen, InputProcessor {
    private GravityGame game;
    private Vector3 mousePosition = new Vector3();
    private float radius = 5;
    private Array<Sprite> spriteArray;
    int frame = 0;
    boolean grow;

    /*
    SpriteBatch
     */
    private SpriteBatch batch;
    private OrthographicCamera camera;

    /*
    particle emitter
     */
    private ParticleEffect effect;
    private float effectScale = 1;

    /*
    sound
     */
    private Sound sound;
    private LongArray soundIds = new LongArray();
    private float frequency;
    private float elapsed = 0;
    private int noteIdx;
    private float[] notes = {55, 65.406f, 82.407f, 97.999f, 123.471f, 146.832f, 174.614f, 220, 261.626f, 329.628f, 391.995f,
            493.883f, 587.33f, 698.456f, 880, 1046.502f, 1318.51f, 1567.982f, 1975.533f, 2349.318f, 2793.826f, 3520};

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

        /*
        texture atlas
         */
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/ball.atlas"));
        spriteArray = atlas.createSprites();

        /*
        particle effect
         */
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("tail.p"), Gdx.files.internal("particles"));
        effect.setPosition(80, 50);
        effect.start();

        /*
        sound
         */
        sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
    }

    /*
    Screen
     */
    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
        /*
        for (int i = 0; i < 3; i++) {
            soundIds.add(sound.loop(0.4f));
            sound.setPitch(i, notes[i + 8] / 440);
        }
        */
        sound.loop(0.4f);
    }

    @Override
    public void render(float delta) {
        if ((int)(radius * 2) >= spriteArray.size - 1)
            grow = false;
        if ((int)(radius * 2) <= 0)
            grow = true;
        if (grow)
            radius += 0.1;
        else
            radius -= 0.1;
        effect.getEmitters().peek().getScale().setHigh(radius * 2.2f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /*
        spritebatch
         */
        batch.begin();
        effect.draw(batch, Gdx.graphics.getDeltaTime());
        spriteArray.get((int)(radius * 2)).setSize(radius * 2, radius * 2);
        spriteArray.get((int)(radius * 2)).setCenter(mousePosition.x, mousePosition.y);
        spriteArray.get((int)(radius * 2)).draw(batch);
        batch.end();

        /*
        sound
        elapsed += Gdx.graphics.getDeltaTime();
        if (elapsed >= 0.25f) {
            noteIdx++;
            elapsed = 0;
        }
        if (noteIdx >= notes.length) {
            noteIdx = 0;
        }
        */
        frequency = notes[notes.length - (int)radius - 3];
        sound.setPitch(0, frequency / 440);
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
        if (keycode == Input.Keys.PLUS) {
            effectScale += 0.1;
            effect.scaleEffect(effectScale);
        }
        if (keycode == Input.Keys.MINUS) {
            effectScale -= 0.1;
            effect.scaleEffect(effectScale);
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
        camera.unproject(mousePosition.set(screenX, screenY, 0));
        effect.setPosition(mousePosition.x, mousePosition.y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

