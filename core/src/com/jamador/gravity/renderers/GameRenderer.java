package com.jamador.gravity.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.jamador.gravity.models.GameWorld;
import com.jamador.gravity.models.GravityObject;

public class GameRenderer {
    private GameWorld world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private Array<GravityObject> gravityObjects;
    private Array<Sprite> stars;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private BitmapFont font;
    private SpriteBatch textRenderer;
    private int screenWidth, screenHeight;
    private Sprite touchpoint;

    public GameRenderer(GameWorld world) {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
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

        /*
        text
         */
        font = new BitmapFont(Gdx.files.internal("font0.fnt"), Gdx.files.internal("font0.png"), false);
        textRenderer = new SpriteBatch();

        /*
        sprites
         */
        touchpoint = new Sprite(new Texture(Gdx.files.internal("touchpoint.png")));
        touchpoint.setSize(15, 15);
        touchpoint.setOriginCenter();
    }

    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);

        /*
        shape renderer
         */
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                shapeRenderer.line(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y);
            }
        shapeRenderer.end();

        /*
        sprite batch
         */
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        /*
        stars
         */
        for (Sprite s : stars) {
            s.draw(batch);
        }
        /*
        touchdown
         */
        if (world.touchDown) {
            touchpoint.setCenter(world.mousePosition.x, world.mousePosition.y);
            touchpoint.setRotation(touchpoint.getRotation() + 2f);
            touchpoint.draw(batch);
        }
        /*
        objects
         */
        for (GravityObject o : gravityObjects) {
            o.render(batch);
        }
        batch.end();

        /*
        score
         */
        textRenderer.begin();
        font.draw(textRenderer, "SCORE: " + world.getScore(), 10, screenHeight - 30);
        font.draw(textRenderer, "POWER " + world.getPower(), 10, screenHeight - 60);
        textRenderer.end();

        /*
        box2d debug
         */
        //debugRenderer.render(world.getWorld(), camera.combined);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
