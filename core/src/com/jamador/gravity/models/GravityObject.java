package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GravityObject {
    GravitySystem system;
    boolean active = true;

    /*
    graphics
     */
    private Sprite arrow;
    private Array<Sprite> ballSprites;
    private int spriteIdx = 10;
    ParticleEffect tail;
    private Color color;

    /*
    physics
     */
    Body body;
    private Fixture fixture;
    private Vector2 singleForce;
    Vector2 netForce;
    float radius;
    Circle bounds;
    float newMass;
    private float growRate;
    boolean growing = false;
    boolean shrinking = false;

    /*
    sound
     */
    Sound sound;
    long soundId;
    float frequency;
    float[] notes = {55, 65.406f, 82.407f, 97.999f, 123.471f, 146.832f, 174.614f, 220, 261.626f, 329.628f, 391.995f,
            493.883f, 587.33f};
    //, 698.456f, 880, 1046.502f, 1318.51f, 1567.982f, 1975.533f, 2349.318f, 2793.826f, 3520
    GravityObject(GravitySystem system, Vector2 position, float mass, Color color) {
        this.system = system;
        this.color = color;
        radius = (float) Math.sqrt(mass / Math.PI);
        bounds = new Circle(position, radius);

        /*
        sound
         */
        sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
        int idx = notes.length - (int)radius - 3;
        if (idx >= notes.length) {
            idx = notes.length - 1;
        }
        if (idx < 0) {
            idx = 0;
        }
        frequency = notes[idx];
        soundId = sound.loop(0.1f);
        sound.setPitch(soundId, notes[idx] / 440);

        /*
        physics
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
        netForce = new Vector2();
        singleForce = new Vector2();

        /*
        graphics
         */
        tail = new ParticleEffect();
        tail.load(Gdx.files.internal("tail.p"), Gdx.files.internal("particles"));
        tail.setPosition(position.x, position.y);
        tail.scaleEffect(radius);
        tail.getEmitters().peek().getScale().setHigh(radius * 2.5f);
        tail.start();
        arrow = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();

    }

    public void update() {
        body.applyForceToCenter(netForce, true);
        bounds.setPosition(body.getPosition());
        tail.setPosition(bounds.x, bounds.y);
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
        sound
         */
        int idx = notes.length - (int)radius - 3;
        if (idx >= notes.length) {
            idx = notes.length - 1;
        }
        if (idx < 0) {
            idx = 0;
        }
        frequency = notes[idx];
        sound.setPitch(soundId, frequency / 440);
    }

    public void render(SpriteBatch batch) {
        /*
        tail
         */
        tail.draw(batch, Gdx.graphics.getDeltaTime());
        /*
        ball
         */
        ballSprites.get(spriteIdx).setRotation(body.getAngle() * 57.3f);
        ballSprites.get(spriteIdx).setCenter(body.getPosition().x, body.getPosition().y);
        //ballSprites.get(spriteIdx).draw(batch);
        if (system.getObjects().size > 1) {
            arrow.setRotation(netForce.angle() + 45 + 180);
            arrow.setPosition(ballSprites.get(spriteIdx).getX(), ballSprites.get(spriteIdx).getY());
        }

        //arrow.draw(batch);
        netForce.set(0,0);
    }

    void destroy() {
        active = false;
        shrinking = false;
        body.destroyFixture(fixture);
        tail.dispose();
        /*
        make new getter for this stupid call
         */
        system.getWorld().getWorld().destroyBody(body);
        sound.stop(soundId);
    }

    void applyGravity(float x, float y, float m) {
        singleForce.set(x, y);
        singleForce.sub(body.getPosition());
        if (singleForce.len() > 1)
            singleForce.setLength((float) (system.g * m/Math.pow(singleForce.len(), 2)));
        netForce.add(singleForce);
    }

    float getMass() {
        return body.getMass();
    }

    Vector2 getPosition() {
        return body.getPosition();
    }

    boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    void startGrow(float m) {
        growing = true;
        shrinking = false;
        newMass = m;
        float newRadius = (float) Math.sqrt(newMass / Math.PI);
        growRate = (newRadius - radius) / (60 * system.growTime);
    }

    void startShrink() {
        shrinking = true;
        growing = false;
        growRate = radius / (60 * system.growTime);
    }

    void grow() {
        radius += growRate;
        tail.getEmitters().peek().getScale().setHigh(radius * 2.5f);
        fixture.getShape().setRadius(radius);
        bounds.setRadius(radius);
        body.resetMassData();
        spriteIdx = (int)(body.getMass() / 8);
        if (spriteIdx >= ballSprites.size) {
            spriteIdx = ballSprites.size - 1;
        }
        ballSprites.get(spriteIdx).setSize(radius*2, radius*2);
        ballSprites.get(spriteIdx).setOriginCenter();
        arrow.setSize(radius*2, radius*2);
        arrow.setOriginCenter();
    }

    void shrink() {
        radius -= growRate;
        tail.getEmitters().peek().getScale().setHigh(radius * 2.5f);
        fixture.getShape().setRadius(radius);
        bounds.setRadius(radius);
        body.resetMassData();
        spriteIdx = (int)(body.getMass() / 8);
        if (spriteIdx >= ballSprites.size) {
            spriteIdx = ballSprites.size - 1;
        }
        ballSprites.get(spriteIdx).setSize(radius * 2, radius * 2);
        ballSprites.get(spriteIdx).setOriginCenter();
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();
    }

    public void setSprites(Array<Sprite> sprites) {
        ballSprites = sprites;
        spriteIdx = (int)(body.getMass() / 8);
        if (spriteIdx >= ballSprites.size) {
            spriteIdx = ballSprites.size - 1;
        }
        for (Sprite s: ballSprites) {
            s.setSize(radius * 2, radius * 2);
            s.setOriginCenter();
            s.setColor(color);
        }
    }
}
