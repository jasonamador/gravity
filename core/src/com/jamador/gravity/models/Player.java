package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

public class Player extends GravityObject{

    public Player(GravitySystem system, Vector2 position, float mass) {
        super(system, position, mass, new Color(1, 1, 1, 1));
        tail.dispose();
        tail = new ParticleEffect();
        tail.load(Gdx.files.internal("player.p"), Gdx.files.internal("particles"));
        tail.setPosition(position.x, position.y);
        tail.scaleEffect(radius);
        tail.getEmitters().peek().getScale().setHigh(radius * 2.2f);
        tail.start();
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
                system.getWorld().gameOver();
            }
        }

        frequency = 1000 - (radius * 100);
        sound.setPitch(soundId, frequency / 500 - 1 + 0.1f);
        sound.setPan(soundId, body.getPosition().x / 80 - 80, 0.4f);
    }
}
