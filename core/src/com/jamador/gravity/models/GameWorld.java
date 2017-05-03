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

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;

/**
 * Created by jason on 3/21/17.
 */
public class GameWorld {
    private World world;
    private GravitySystem gravitySystem;
    private Array<Sprite> stars;
    private Player player;
    private float playerMass = 500;
    private SpriteBatch batch;
    private float power = 100;
    private int score;

    /*
    controller
     */
    public boolean touchDown;
    public Vector3 mousePosition;

    public GameWorld() {
        score = 0;

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
                if (contact.getFixtureA().getBody().getUserData() instanceof GravityObject
                        && contact.getFixtureA().getBody().getUserData() instanceof GravityObject) {
                    a = (GravityObject) contact.getFixtureA().getBody().getUserData();
                    b = (GravityObject) contact.getFixtureB().getBody().getUserData();
                    if (a.getMass() > b.getMass()) {
                        if (a.equals(player)) {
                            score += 10 * b.getMass();
                        }
                        a.startGrow(b.getMass() + a.getMass());
                        b.startShrink();
                    } else {
                        b.startGrow(a.getMass() + b.getMass());
                        a.startShrink();
                    }
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
        arena
         */
        BodyDef bd = new BodyDef();
        PolygonShape rect = new PolygonShape();

        /*
        bottom
         */
        bd.position.set(new Vector2(0, 1f));
        Body bottomBody = world.createBody(bd);
        rect.setAsBox(160f, 1f);
        bottomBody.createFixture(rect, 1.0f);
        //rect.dispose();

        /*
        top
         */
        bd.position.set(new Vector2(0, 99f));
        Body topBody = world.createBody(bd);
        rect.setAsBox(160f, 1f);
        topBody.createFixture(rect, 1.0f);
        //rect.dispose();

        /*
        left
         */
        bd.position.set(new Vector2(1, 0f));
        Body leftBody = world.createBody(bd);
        rect.setAsBox(1f, 160f);
        leftBody.createFixture(rect, 1.0f);
        //rect.dispose();

        /*
        right
         */
        bd.position.set(new Vector2(159, 0f));
        Body rightBody = world.createBody(bd);
        rect.setAsBox(1f, 160f);
        rightBody.createFixture(rect, 1.0f);
        //rect.dispose();

        /*
        create gravity system
         */
        Random r = new Random();
        gravitySystem = new GravitySystem(this, new Array<GravityObject>());
        player = new Player(gravitySystem, new Vector2(80, 50), 35f);

        for (int x=0; x<15; x++) {
            gravitySystem.add(new GravityObject(gravitySystem, new Vector2(r.nextFloat() * 160,
                    r.nextFloat() * 100), r.nextFloat() * 50 + 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1)));
        }
        gravitySystem.add(player);

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
        if (touchDown && power > 0) {
            power -= 0.1;
            gravitySystem.applyGravity(mousePosition.x, mousePosition.y, playerMass);
        }
        gravitySystem.update();
        world.step(1/60f, 6, 2);
    }

    public void gameOver() {
        Random r = new Random();
        gravitySystem.reset();
        for (int x=0; x<15; x++) {
            gravitySystem.add(new GravityObject(gravitySystem, new Vector2(r.nextFloat() * 160,
                    r.nextFloat() * 100), r.nextFloat() * 50 + 1, new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1)));
        }
        player = new Player(gravitySystem, new Vector2(80, 50), 35f);
        gravitySystem.add(player);
    }

    public Player getPlayer() {
        return player;
    }

    public Array<GravityObject> getGravityObjects() {
        return gravitySystem.getObjects();
    }

    public Array<Sprite> getStars() {
        return stars;
    }

    public World getWorld() {
        return world;
    }

    public int getScore() {
        return score;
    }

    public float getPower() {
        return power;
    }
}
