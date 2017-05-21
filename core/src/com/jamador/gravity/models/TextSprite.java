package com.jamador.gravity.models;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TextSprite {
    private GlyphLayout layout;
    private Rectangle bounds;
    private Vector2 center;
    public TextSprite(String text, BitmapFont font, Vector2 center) {
        this.center = center;
        layout = new GlyphLayout(font, text);
        bounds = new Rectangle(center.x, center.y, layout.width, layout.height);
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
}
