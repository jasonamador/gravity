package com.jamador.gravity.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by jason on 3/19/17.
 */
public class Player extends GravityObject{

    public Player(GameWorld world, Vector2 position, float mass) {
        super(world, position, mass, new Color(1, 1, 1, 1));
    }
}
