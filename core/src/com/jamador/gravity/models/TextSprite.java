package com.jamador.gravity.models;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TextSprite {
    private GlyphLayout layout;
    private Rectangle bounds;
    private BitmapFont font;
    private Vector2 center;
    private Vector2 topLeft;
    private Vector2 bottomLeft;

    public TextSprite(String text, BitmapFont font, Vector2 center) {
        this.center = center;
        this.font = font;
        layout = new GlyphLayout(font, text);
        topLeft = new Vector2(center.x - (layout.width / 2), center.y + (layout.height / 2));
        bottomLeft = new Vector2(topLeft.x, topLeft.y - layout.height);
        bounds = new Rectangle(bottomLeft.x, bottomLeft.y, layout.width, layout.height);
    }

    /*
    getters
     */
    public float getWidth() {
        return layout.width;
    }

    public float getHeight() {
        return layout.height;
    }

    public boolean contains(Vector2 p) {
        return bounds.contains(p);
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, layout, topLeft.x, topLeft.y);
    }
}
