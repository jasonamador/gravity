package com.jamador.gravity.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.jamador.gravity.models.GameWorld;
import com.jamador.gravity.models.GravityObject;
import com.jamador.gravity.models.Player;

/**
 * Created by jason on 3/21/17.
 */
public class GameRenderer {
    private GameWorld world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private Player player;
    private Array<GravityObject> gravityObjects;
    private Array<Sprite> stars;

    public GameRenderer(GameWorld world) {
        this.world = world;
        player = world.getPlayer();
        gravityObjects = world.getGravityObjects();
        stars = world.getStars();

        camera = new OrthographicCamera(160f, 100f);
        camera.position.set(80f, 50f, 0f);
        camera.update();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        debugRenderer = new Box2DDebugRenderer();
    }

    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(player.getPosition(), 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite s : stars) {
            s.draw(batch);
        }
        for (GravityObject o : gravityObjects) {
            o.getSprite().draw(batch);
        }
        batch.end();
        //debugRenderer.render(world, camera.combined);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
