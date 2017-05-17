package com.jamador.gravity.models;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextSprite {
    private GlyphLayout layout;
    public TextSprite(String text, BitmapFont font) {
        layout = new GlyphLayout(font, text);
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
