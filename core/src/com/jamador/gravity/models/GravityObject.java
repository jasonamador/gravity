package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GravityObject {
    GravitySystem system;
    private Array<Sprite> path;
    private Texture pathTexture;
    private Sprite arrow;
    Body body;
    private Fixture fixture;
    private Vector2 singleForce;
    Sound sound;
    long soundId;
    float frequency;
    Vector2 netForce;
    float radius;
    float newMass;
    private float growRate;
    Circle bounds;
    boolean growing = false;
    boolean shrinking = false;
    boolean active = true;
    private Array<Sprite> ballSprites;
    private int spriteIdx = 10;
    private Color color;

    GravityObject(GravitySystem system, Vector2 position, float mass, Color color) {
        this.system = system;
        this.color = color;
        radius = (float) Math.sqrt(mass / Math.PI);

        /*
        sound
         */
        sound = Gdx.audio.newSound(Gdx.files.internal("sound.wav"));
        frequency = 1000 - (radius * 100);
        soundId = sound.loop(0.4f);

        /*
        bounds
         */
        bounds = new Circle(position, radius);

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

        /*
        sprites
         */
        arrow = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();
        /*
        path = new Array<Sprite>();
        pathTexture = new Texture(Gdx.files.internal("ball.png"));
        */

        netForce = new Vector2();
        singleForce = new Vector2();
    }

    public void update() {
        body.applyForceToCenter(netForce, true);
        bounds.setPosition(body.getPosition());
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
        path
        lifetime++;
        if (path.size > 10) {
            path.removeIndex(0);
        }
        if (lifetime % 30 == 0) {
            lifetime = 0;
            path.add(new Sprite(pathTexture));
            path.peek().setPosition(ball.getX(), ball.getY());
            path.peek().setSize(radius * 2, radius * 2);
            path.peek().setOriginCenter();
            path.peek().setColor(0.1f, 0.9f,1.0f, 1);
        }
        for (Sprite s: path) {
            s.setSize(s.getWidth() * .99f, s.getHeight() * .99f);
            s.setOriginCenter();
        }

         */
        /*
        sound
         */
        frequency = 1000 - (radius * 100);
        sound.setPitch(soundId, frequency / 500 - 1 + 0.1f);
        sound.setPan(soundId, body.getPosition().x / 80 - 80, 0.4f);
    }

    public void render(SpriteBatch batch) {
        /*
        path
        for (Sprite s: path) {
            s.draw(batch);
        }
         */

        /*
        ball
         */
        ballSprites.get(spriteIdx).setRotation(body.getAngle() * 57.3f);
        ballSprites.get(spriteIdx).setCenter(body.getPosition().x, body.getPosition().y);
        ballSprites.get(spriteIdx).draw(batch);
        if (system.getObjects().size > 1) {
            arrow.setRotation(netForce.angle() + 45 + 180);
            arrow.setPosition(ballSprites.get(spriteIdx).getX(), ballSprites.get(spriteIdx).getY());
        }

        /*
        arrow
         */
        arrow.draw(batch);

        netForce.set(0,0);
    }

    void destroy() {
        active = false;
        shrinking = false;
        body.destroyFixture(fixture);
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
        System.out.println(radius);
        spriteIdx = (int)(radius * 4);
        if (spriteIdx >= ballSprites.size) {
            spriteIdx = ballSprites.size - 1;
        }
        fixture.getShape().setRadius(radius);
        bounds.setRadius(radius);
        body.resetMassData();
        ballSprites.get(spriteIdx).setSize(radius*2, radius*2);
        ballSprites.get(spriteIdx).setOriginCenter();
        arrow.setSize(radius*2, radius*2);
        arrow.setOriginCenter();
    }

    void shrink() {
        radius -= growRate;
        spriteIdx = (int)(radius * 4);
        if (spriteIdx >= ballSprites.size) {
            spriteIdx = ballSprites.size - 1;
        }
        fixture.getShape().setRadius(radius);
        bounds.setRadius(radius);
        body.resetMassData();
        ballSprites.get(spriteIdx).setSize(radius * 2, radius * 2);
        ballSprites.get(spriteIdx).setOriginCenter();
        arrow.setSize(radius * 2, radius * 2);
        arrow.setOriginCenter();
    }

    public void setSprites(Array<Sprite> sprites) {
        ballSprites = sprites;
        spriteIdx = (int)(radius * 4);
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
