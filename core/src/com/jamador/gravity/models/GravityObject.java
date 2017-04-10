package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import java.util.Random;

/**
 * Created by jason on 3/20/17.
 */
public class GravityObject {
    private GameWorld world;
    private Sprite ball;
    private Sprite arrow;
    private Body body;
    private Fixture fixture;
    private SpriteBatch batch;
    private Vector2 netForce, singleForce;
    private float radius;
    private float mass;
    private float newMass;
    private float maxForceFactor;
    private float g = 40;
    private float growRate;
    private float growTime = 0.5f;
    public boolean growing = false;
    public boolean shrinking = false;
    public boolean active = true;

    public GravityObject(GameWorld world, SpriteBatch batch, Vector2 position, float mass) {
        this.world = world;
        this.batch = batch;
        this.mass = mass;
        radius = (float) Math.sqrt(mass / Math.PI);

        /*
        box2d definitions
         */
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(position);
        body = world.getWorld().createBody(bd);
        body.setUserData(this);

        FixtureDef fd = new FixtureDef();
        fd.shape = new CircleShape();
        fd.shape.setRadius(radius);
        fd.density = 1f;
        fd.friction = 0.6f;
        fd.restitution = 0.3f;
        fixture = body.createFixture(fd);

        Random r = new Random();
        body.setLinearVelocity(new Vector2(r.nextFloat() * 3, r.nextFloat() * 3));

        ball = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        ball.setSize(radius * 2, radius * 2);
        ball.setOriginCenter();
        arrow = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();

        netForce = new Vector2();
        singleForce = new Vector2();
        maxForceFactor = 1000;
    }

    public void update() {
        float maxForce = maxForceFactor * body.getMass();
        if (netForce.len2() > maxForce) {
            netForce.setLength2(maxForce);
        }
        body.applyForceToCenter(netForce, true);
        if(growing) {
            if (body.getMass() < newMass)
                grow();
            else
                growing = false;
        }
        if(shrinking) {
            if (radius > 0.1)
                shrink();
            else {
                active = false;
                shrinking = false;
                body.destroyFixture(fixture);
                world.getWorld().destroyBody(body);
                //game over
            }
        }
    }

    public void render(SpriteBatch batch) {
        /*renderer*/
        ball.setRotation(body.getAngle() * 57.3f);
        ball.setCenter(body.getPosition().x, body.getPosition().y);
        ball.draw(batch);
        arrow.setRotation(netForce.angle() + 45 + 180);
        arrow.setPosition(ball.getX(), ball.getY());
        float alpha = netForce.len2() / (maxForceFactor / 3 * body.getMass());
        arrow.setAlpha(alpha > 1 ? 1 : alpha);
        arrow.draw(batch);
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

    public Sprite getSprite() {
        return ball;
    }

    public void startGrow(float m) {
        growing = true;
        shrinking = false;
        newMass = m;
        float newRadius = (float) Math.sqrt(newMass / Math.PI);
        growRate = (newRadius - radius) / (60 * growTime);
    }

    public void startShrink() {
        shrinking = true;
        growing = false;
        growRate = radius / (60 * growTime);
    }

    private void grow() {
        radius += growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        ball.setSize(radius*2, radius*2);
        ball.setOriginCenter();
        arrow.setSize(radius*2, radius*2);
        arrow.setOriginCenter();
    }

    private void shrink() {
        radius -= growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        ball.setSize(radius * 2, radius * 2);
        ball.setOriginCenter();
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();
    }
}
