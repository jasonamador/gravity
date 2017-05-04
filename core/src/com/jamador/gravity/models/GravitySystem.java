package com.jamador.gravity.models;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GravitySystem {
    private Array<GravityObject> gravityObjects;
    private GameWorld world;
    float g = 40f;
    float growTime = 0.5f;

    public GravitySystem(GameWorld world, Array<GravityObject> gos) {
        gravityObjects = gos;
        this.world = world;
    }

    public void add(GravityObject o) {
        gravityObjects.add(o);
    }

    void applyGravity(float x, float y, float m) {
        for (GravityObject o : gravityObjects) {
            if (!o.contains(x, y)) {
                o.applyGravity(x, y, m);
            }
        }
    }

    Array<GravityObject> getObjects() {
        return gravityObjects;
    }

    public void update() {
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                gravityObjects.get(i).applyGravity(gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y, gravityObjects.get(j).getMass());
                gravityObjects.get(j).applyGravity(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(i).getMass());
            }
        for (int i = 0; i < gravityObjects.size; i++) {
            if (!gravityObjects.get(i).active) {
                gravityObjects.removeValue(gravityObjects.get(i), true);
            }
            gravityObjects.get(i).update();
        }
    }

    public GameWorld getWorld() {
        return world;
    }

    World get2DWorld() {
        return world.getWorld();
    }

    void reset() {
        for (GravityObject o: gravityObjects) {
            o.destroy();
        }
        gravityObjects.clear();
    }
}
