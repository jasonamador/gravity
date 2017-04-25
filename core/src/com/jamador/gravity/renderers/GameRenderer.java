package com.jamador.gravity.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private Array<GravityObject> gravityObjects;
    private Array<Sprite> stars;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public GameRenderer(GameWorld world) {
        this.world = world;
        gravityObjects = world.getGravityObjects();
        stars = world.getStars();
        camera = new OrthographicCamera(160f, 100f);
        camera.position.set(80f, 50f, 0f);
        camera.update();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        debugRenderer = new Box2DDebugRenderer();
        /*
        Shape Renderer
        */
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(.4f, .6f, .7f, .5f);
    }

    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);

        camera.position.set(world.getPlayer().getPosition(), 0);
        camera.update();

        //shape renderer
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                shapeRenderer.line(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y);
            }
        shapeRenderer.end();

        //sprite batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite s : stars) {
            s.draw(batch);
        }

        for (GravityObject o : gravityObjects) {
            o.render(batch);
        }
        batch.end();
        //debugRenderer.render(world, camera.combined);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
