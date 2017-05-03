package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    protected GravitySystem system;
    protected Sprite ball;
    protected Sprite arrow;
    protected Body body;
    protected Fixture fixture;
    protected Vector2 netForce, singleForce;
    protected float radius;
    protected float newMass;
    protected float growRate;
    public boolean growing = false;
    public boolean shrinking = false;
    public boolean active = true;

    public GravityObject(GravitySystem system, Vector2 position, float mass, Color color) {
        this.system = system;
        radius = (float) Math.sqrt(mass / Math.PI);

        /*
        box2d definitions
         */
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        bd.position.set(position);
        body = system.get2DWorld().createBody(bd);
        body.setUserData(this);

        FixtureDef fd = new FixtureDef();
        fd.shape = new CircleShape();
        fd.shape.setRadius(radius);
        fd.density = 1f;
        fd.friction = 0.6f;
        fd.restitution = 1.0f;
        fixture = body.createFixture(fd);

        Random r = new Random();
        body.setLinearVelocity(new Vector2(r.nextFloat() * 3, r.nextFloat() * 3));

        ball = new Sprite(new Texture(Gdx.files.internal("ball.png")));
        ball.setSize(radius * 2, radius * 2);
        ball.setOriginCenter();
        ball.setColor(color);
        arrow = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();

        netForce = new Vector2();
        singleForce = new Vector2();
    }

    public void update() {
        body.applyForceToCenter(netForce, true);
        if (netForce.len2() / body.getMass() > system.maxAccel) {
            netForce.setLength2(body.getMass() * system.maxAccel);
        }
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
                destroy();
            }
        }
        /*
        max velocity logic
         */
        if (body.getLinearVelocity().len2() >= system.maxVelocity) {
            Vector2 newVelocity = body.getLinearVelocity();
            newVelocity.setLength2(system.maxVelocity);
            body.setLinearVelocity(newVelocity);
        }
    }

    public void render(SpriteBatch batch) {
        /*renderer*/
        ball.setRotation(body.getAngle() * 57.3f);
        ball.setCenter(body.getPosition().x, body.getPosition().y);
        ball.draw(batch);
        if (system.getObjects().size > 1) {
            arrow.setRotation(netForce.angle() + 45 + 180);
            arrow.setPosition(ball.getX(), ball.getY());
        }
        /*
        float alpha = netForce.len2() / maxForceFactor;
        arrow.setAlpha(alpha > 1 ? 1 : alpha);
        */
        arrow.draw(batch);
        netForce.set(0,0);
    }

    //this should probably be a GravitySystem method
    public void destroy() {
        active = false;
        shrinking = false;
        body.destroyFixture(fixture);
        system.g += body.getMass();
        // make a new getter for this
        system.getWorld().getWorld().destroyBody(body);
    }

    public void applyGravity(float x, float y, float m) {
        singleForce.set(x, y);
        singleForce.sub(body.getPosition());
        if (singleForce.len() > 1)
            singleForce.setLength((float) (system.g * m/Math.pow(singleForce.len(), 2)));
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
        float newRadius = (float) Math.sqrt(newMass / Math.PI);
        growRate = (newRadius - radius) / (60 * system.growTime);
    }

    public void startShrink() {
        shrinking = true;
        growing = false;
        growRate = radius / (60 * system.growTime);
    }

    protected void grow() {
        radius += growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        ball.setSize(radius*2, radius*2);
        ball.setOriginCenter();
        arrow.setSize(radius*2, radius*2);
        arrow.setOriginCenter();
    }

    protected void shrink() {
        radius -= growRate;
        fixture.getShape().setRadius(radius);
        body.resetMassData();
        ball.setSize(radius * 2, radius * 2);
        ball.setOriginCenter();
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();
    }
}
