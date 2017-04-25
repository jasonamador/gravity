package com.jamador.gravity.models;

import com.badlogic.gdx.utils.Array;

/**
 * Created by jason on 4/24/17.
 */
public class GravityObjects {
    private Array<GravityObject> gravityObjects;

    public GravityObjects(Array<GravityObject> gos) {
        gravityObjects = gos;
    }

    public void add(GravityObject o) {
        gravityObjects.add(o);
    }

    public GravityObject get(int i) {
        return gravityObjects.get(i);
    }

    public void applyGravity(float x, float y, float m) {
        for (GravityObject o : gravityObjects) {
            o.applyGravity(x, y, m);
        }
    }

    public Array<GravityObject> getArray() {
        return gravityObjects;
    }

    public void update() {
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                gravityObjects.get(i).applyGravity(gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y, gravityObjects.get(j).getMass());
                gravityObjects.get(j).applyGravity(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(i).getMass());
            }
        for (GravityObject o : gravityObjects) {
            if (!o.active) {
                gravityObjects.removeValue(o, true);
            }
            o.update();
        }
    }

}
