package com.jamador.gravity;

import com.badlogic.gdx.Game;
import com.jamador.gravity.screens.GameScreen;
import com.jamador.gravity.screens.MenuScreen;
import com.jamador.gravity.screens.TestScreen;

public class GravityGame extends Game {
    public MenuScreen menuScreen;
    public GameScreen gameScreen;
    public TestScreen testScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);
        testScreen = new TestScreen(this);
        setScreen(menuScreen);
    }
}
