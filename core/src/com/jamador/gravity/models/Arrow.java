package com.jamador.gravity.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by jason on 3/28/17.
 */
public class Arrow {
    private Sprite sprite;
    private Vector2 force;
    private GravityObject object;

    public Arrow(Vector2 force, GravityObject object) {
        this.force = force;
        this.object = object;
        sprite = new Sprite(new Texture(Gdx.files.internal("arrow.png")));
        sprite.setSize(object.getSprite().getWidth(), object.getSprite().getHeight());
    }

    public void update(Vector2 f) {
        sprite.setSize(object.getSprite().getWidth(), object.getSprite().getHeight());
        sprite.setPosition(object.getSprite().getX(), object.getSprite().getY());
        sprite.setRotation(force.angle() + 45);
        sprite.setAlpha(force.len2() / 20);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
