package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by jason on 3/19/17.
 */
public class Player {
    private Sprite sprite;
    private Body body;
    private SpriteBatch batch;
    private Vector2 force;

    public Player(World world, SpriteBatch batch) {
        this.batch = batch;

        /*
        box2d definitions
         */
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(300, 300);
        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.shape = new CircleShape();
        fd.shape.setRadius(64f);
        fd.density = 0.00005f;
        fd.friction = 0.5f;
        body.createFixture(fd);

        sprite = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        force = new Vector2();
    }

    public void render() {
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
        body.applyForceToCenter(force, true);
    }

    public void zeroForce() {
        force.set(0f, 0f);
    }

    public void applyForce(Vector2 p) {
        force.set(body.getPosition().x, body.getPosition().y);
        force.sub(p);
        force.setAngle(force.angle() + 180);
        force.setLength2(1000);
    }
}
