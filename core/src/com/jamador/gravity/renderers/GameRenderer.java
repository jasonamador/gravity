package com.jamador.gravity.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.jamador.gravity.models.GameWorld;
import com.jamador.gravity.models.GravityObject;

import java.util.Random;

public class GameRenderer {
    private GameWorld world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private Array<GravityObject> gravityObjects;
    private TextureAtlas ballAtlas;
    private Array<Sprite> stars;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private BitmapFont font100, font75, font50, font35, font25, font20;
    private SpriteBatch textRenderer;
    private int screenWidth, screenHeight;
    private Sprite touchpoint;

    public GameRenderer(GameWorld world) {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        ballAtlas = new TextureAtlas(Gdx.files.internal("textures/ball.atlas"));
        this.world = world;
        gravityObjects = world.getGravityObjects();
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
        font20 = new BitmapFont(Gdx.files.internal("font5.fnt"), Gdx.files.internal("font5.png"), false);
        font25 = new BitmapFont(Gdx.files.internal("font4.fnt"), Gdx.files.internal("font4.png"), false);
        font35 = new BitmapFont(Gdx.files.internal("font3.fnt"), Gdx.files.internal("font3.png"), false);
        font50 = new BitmapFont(Gdx.files.internal("font2.fnt"), Gdx.files.internal("font2.png"), false);
        font75 = new BitmapFont(Gdx.files.internal("font1.fnt"), Gdx.files.internal("font1.png"), false);
        font100 = new BitmapFont(Gdx.files.internal("font0.fnt"), Gdx.files.internal("font0.png"), false);
        textRenderer = new SpriteBatch();

        /*
        touchpoint
         */
        touchpoint = new Sprite(new Texture(Gdx.files.internal("touchpoint.png")));
        touchpoint.setSize(15, 15);
        touchpoint.setOriginCenter();

        /*
        stars
         */
        Random r = new Random();
        Sprite star = new Sprite(new Texture(Gdx.files.internal("star.png")));
        star.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        stars = new Array<Sprite>();
        float size, x, y;
        for (int i=0; i < 200; i++) {
            size = r.nextFloat() * 1.5f;
            x = r.nextFloat() * 160;
            y = r.nextFloat() * 100;
            star.setPosition(x, y);
            star.setSize(size, size);
            stars.add(new Sprite(star));
        }

        for (GravityObject o: gravityObjects) {
            o.setSprites(ballAtlas.createSprites());
        }
    }

    public void render() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glEnable(GL20.GL_BLEND);

        /*
        shape renderer
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();
        for (int i = 0; i < gravityObjects.size; i++)
            for (int j = i; j < gravityObjects.size; j++) {
                shapeRenderer.line(gravityObjects.get(i).getPosition().x, gravityObjects.get(i).getPosition().y, gravityObjects.get(j).getPosition().x, gravityObjects.get(j).getPosition().y);
            }
        shapeRenderer.end();
         */

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
        font50.draw(textRenderer, "SCORE: " + world.getScore(), 10, screenHeight - 30);
        font50.draw(textRenderer, "POWER " + (int)world.getPower() + "%", 10, screenHeight - 60);
        textRenderer.end();

        /*
        box2d debug
         */
        //debugRenderer.render(world.getWorld(), camera.combined);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void reset() {
        for (GravityObject o: gravityObjects) {
            o.setSprites(ballAtlas.createSprites());
        }
    }
}
