package com.jamador.gravity;

import com.badlogic.gdx.Game;
import com.jamador.gravity.screens.GameScreen;
import com.jamador.gravity.screens.MainMenuScreen;

public class GravityGame extends Game {
    public MainMenuScreen menuScreen;
    public GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        menuScreen = new MainMenuScreen(this);
        setScreen(menuScreen);
    }
}
