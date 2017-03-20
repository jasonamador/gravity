package com.jamador.gravity;

import com.badlogic.gdx.Game;
import com.jamador.gravity.screens.MainMenuScreen;

public class GravityGame extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
