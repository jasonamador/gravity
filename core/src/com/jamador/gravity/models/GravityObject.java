package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by jason on 3/20/17.
 */
public class GravityObject {
    private Sprite sprite;
    private Body body;
    private Fixture fixture;
    private SpriteBatch batch;
    private Vector2 netForce, singleForce;
    private float radius;
    private float mass;
    private float newMass;
    private float g = 25;
    private float growRate = 0.1f;

    public boolean growing = false;
    public boolean shrinking = false;
    public boolean active = true;

    public GravityObject(World world, SpriteBatch batch, Vector2 position, float mass) {
        this.batch = batch;
        this.mass = mass;
        radius = (float) Math.sqrt(mass / Math.PI);

        /*
        box2d definitions
         */
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(position);
        body = world.createBody(bd);
        body.setUserData(this);

        FixtureDef fd = new FixtureDef();
        fd.shape = new CircleShape();
        fd.shape.setRadius(radius);
        fd.density = 1f;
        fd.friction = 0.5f;
        fixture = body.createFixture(fd);

        sprite = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        sprite.setSize(radius * 2, radius * 2);
        netForce = new Vector2();
        singleForce = new Vector2();
    }

    public void render() {
        sprite.setCenter(body.getPosition().x, body.getPosition().y);
        sprite.draw(batch);
    }

    public void update() {
        body.applyForceToCenter(netForce, true);
        if(growing) {
            if (body.getMass() < newMass)
                grow();
            else
                growing = false;
        }
        if(shrinking) {
            if (radius > growRate)
                shrink();
            else {
                active = false;
                shrinking = false;
                body.destroyFixture(fixture);
            }
        }
        netForce.set(0,0);
    }

    public void applyGravity(float x, float y, float m) {
        singleForce.set(x, y);
        singleForce.sub(body.getPosition());
        if (singleForce.len() > 1)
            singleForce.setLength((float) (g * m/Math.pow(singleForce.len(), 2)));
        netForce.add(singleForce);
    }

    public float getMass() {
        return body.getMass();
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void startGrow(float m) {
        growing = true;
        shrinking = false;
        newMass = m;
    }

    public void startShrink() {
        shrinking = true;
        growing = false;
    }

    private void grow() {
        /*
        mass += growRate;
        radius = (float) Math.sqrt(mass / Math.PI);
        */
        radius += growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        sprite.setSize(radius*2, radius*2);
    }

    private void shrink() {
        /*
        mass -= growRate;
        radius = (float) Math.sqrt(mass / Math.PI);
        */
        radius -= growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        sprite.setSize(radius*2, radius*2);
    }
}
