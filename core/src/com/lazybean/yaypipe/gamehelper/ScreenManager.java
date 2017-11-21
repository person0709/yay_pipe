package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Screen;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.screens.GamePlayScreen;
import com.lazybean.yaypipe.screens.GameSetupScreen;
import com.lazybean.yaypipe.screens.MainMenuScreen;
import com.lazybean.yaypipe.screens.StatisticsScreen;

public class ScreenManager {
    private MainMenuScreen mainMenuScreen;
    private GameSetupScreen gameSetUpScreen;
    private StatisticsScreen statisticsScreen;

    public ScreenManager(YayPipe game){
        mainMenuScreen = new MainMenuScreen(game);
        gameSetUpScreen = new GameSetupScreen(game);
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public GameSetupScreen getGameSetUpScreen() {
        return gameSetUpScreen;
    }

    public StatisticsScreen getStatisticsScreen() {
        return statisticsScreen;
    }

    public void dispose(){
        if (mainMenuScreen != null) {
            mainMenuScreen.dispose();
        }
        if (gameSetUpScreen != null) {
            gameSetUpScreen.dispose();
        }
        if (statisticsScreen != null) {
            statisticsScreen.dispose();
        }
    }
}
