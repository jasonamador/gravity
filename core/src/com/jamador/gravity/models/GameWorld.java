package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


import java.util.Random;

/**
 * Created by jason on 3/21/17.
 */
public class GameWorld {
    private World world;
    //private Array<GravityObject> gravityObjects;
    private GravityObjects gravityObjects;
    private Array<Sprite> stars;
    private Player player;
    private float playerMass = 500;
    private SpriteBatch batch;
    private float power = 100;

    /*
    controller
     */
    public boolean touchDown;
    public Vector3 mousePosition;

    public GameWorld() {
        /*
        controller
         */
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        touchDown = false;

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
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        /*
        level loader
        TiledMap map = new TmxMapLoader().load("test.tmx");
        MapBodyManager mapBodyManager = new MapBodyManager(world, 160 / 800, Gdx.files.internal("materials.json"), 0);
        //mapBodyManager.createPhysics(map, "physics");
        */

        /*
        game objects
         */
        player = new Player(this, new Vector2(80, 50), 35f);

        Random r = new Random();
        gravityObjects = new GravityObjects(new Array<GravityObject>());

        for (int x=0; x<15; x++) {
            gravityObjects.add(new GravityObject(this, new Vector2(r.nextFloat() * 160,
                    r.nextFloat() * 100), r.nextFloat() * 50 + 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1)));
        }
        gravityObjects.add(player);

        /*
        stars
         */
        Sprite star = new Sprite(new Texture(Gdx.files.internal("star.png")));
        star.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        stars = new Array<Sprite>();
        float size, x, y;
        for (int i=0; i < 400; i++) {
            size = r.nextFloat() * 1.5f;
            x = r.nextFloat() * 480 - 160;
            y = r.nextFloat() * 300 - 100;
            star.setPosition(x, y);
            star.setSize(size, size);
            stars.add(new Sprite(star));
        }
    }

    public void update() {
        if (touchDown) {
            power -= 0.1;
            System.out.println(power);
        }
        if (touchDown && power > 0) {
            gravityObjects.applyGravity(mousePosition.x, mousePosition.y, playerMass);
        }
        gravityObjects.update();
        world.step(1/60f, 6, 2);
    }

    public Player getPlayer() {
        return player;
    }

    public Array<GravityObject> getGravityObjects() {
        return gravityObjects.getArray();
    }

    public Array<Sprite> getStars() {
        return stars;
    }

    public World getWorld() {
        return world;
    }
}
