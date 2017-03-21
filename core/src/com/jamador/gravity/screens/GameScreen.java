package com.jamador.gravity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.jamador.gravity.GravityGame;
import com.jamador.gravity.models.GravityObject;
import com.jamador.gravity.models.Player;
import com.siondream.core.physics.MapBodyManager;

import java.util.Random;

/**
 * Created by jason on 3/19/17.
 */
public class GameScreen implements Screen, InputProcessor {
    final GravityGame game;

    private int screenWidth, screenHeight;

    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private Array<GravityObject> gravityObjects;
    private Array<Sprite> stars;
    private Player player;

    private Vector3 mousePosition;
    private boolean touchDown;

    private float playerMass = 4000;

    private SpriteBatch batch;

    public GameScreen(GravityGame game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);

       /*
        setup graphics
         */
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(160f, 100f);
        camera.position.set(80f, 50f, 0f);
        camera.update();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        mousePosition = new Vector3(80f, 50f, 0f);

        /*
        box2d
         */
        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(new ContactListener() {
            private GravityObject a, b;
            @Override
            public void beginContact(Contact contact) {
                a = (GravityObject) contact.getFixtureA().getBody().getUserData();
                b = (GravityObject) contact.getFixtureB().getBody().getUserData();
                if (a.getMass() > b.getMass()) {
                    a.startGrow(b.getMass() + a.getMass());
                    b.startShrink();
                } else {
                    b.startGrow(a.getMass() + b.getMass());
                    a.startShrink();
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
        debugRenderer = new Box2DDebugRenderer();

        /*
        level loader
         */
        TiledMap map = new TmxMapLoader().load("test.tmx");
        MapBodyManager mapBodyManager = new MapBodyManager(world, 160 / 800, Gdx.files.internal("materials.json"), 0);
        //mapBodyManager.createPhysics(map, "physics");

        /*
        game objects
         */
        player = new Player(world, batch, new Vector2(80, 50), 35f);

        Random r = new Random();
        gravityObjects = new Array<GravityObject>();
        /*
        gravityObjects.add(new GravityObject(world, batch, new Vector2(70, 50), 50));
        gravityObjects.add(new GravityObject(world, batch, new Vector2(90, 50), 30));
        */
        for (int x=0; x<15; x++) {
            gravityObjects.add(new GravityObject(world, batch, new Vector2(r.nextFloat() * 160,
                    r.nextFloat() * 100), r.nextFloat() * 50 + 1));
        }
        gravityObjects.add(player);

        /*
        background
         */
        Sprite star = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        star.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        stars = new Array<Sprite>();
        float size, x, y;
        for (int i=0; i < 400; i++) {
            size = r.nextFloat() * 1;
            x = r.nextFloat() * 480 - 160;
            y = r.nextFloat() * 300 - 100;
            star.setPosition(x, y);
            star.setSize(size, size);
            stars.add(new Sprite(star));
        }

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
        for (GravityObject o : gravityObjects) {
            if (!o.active) {
                gravityObjects.removeValue(o, true);
            }
            if (touchDown)
                o.applyGravity(mousePosition.x, mousePosition.y, playerMass);
            o.update();
        }
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                gravityObjects.get(i).applyGravity(gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y, gravityObjects.get(j).getMass());
                gravityObjects.get(j).applyGravity(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(i).getMass());
            }
        camera.position.set(player.getPosition(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite s : stars) {
            s.draw(batch);
        }
        for (GravityObject o : gravityObjects) {
            o.render();
        }
        batch.end();
        //debugRenderer.render(world, camera.combined);
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
        camera.unproject(mousePosition.set(screenX, screenY, 0));
        touchDown = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(mousePosition.set(screenX, screenY, 0));
        touchDown = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        camera.unproject(mousePosition.set(screenX, screenY, 0));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        camera.unproject(mousePosition.set(screenX, screenY, 0));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
