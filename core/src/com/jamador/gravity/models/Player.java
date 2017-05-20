package com.jamador.gravity.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Player extends GravityObject{

    public Player(GravitySystem system, Vector2 position, float mass) {
        super(system, position, mass, new Color(1, 1, 1, 1));
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
                system.getWorld().gameOver();
            }
        }
        /*
        max velocity logic
        if (body.getLinearVelocity().len2() >= system.maxVelocity) {
            Vector2 newVelocity = body.getLinearVelocity();
            newVelocity.setLength2(system.maxVelocity);
            body.setLinearVelocity(newVelocity);
        }
         */
    }
}
