package com.lazybean.yaypipe.gamehelper;

import com.badlogic.gdx.Screen;
import com.lazybean.yaypipe.YayPipe;
import com.lazybean.yaypipe.screens.GamePlayScreen;
import com.lazybean.yaypipe.screens.GameSetupScreen;
import com.lazybean.yaypipe.screens.MainMenuScreen;
import com.lazybean.yaypipe.screens.SplashScreen;
import com.lazybean.yaypipe.screens.StatisticsScreen;

public class ScreenManager {
    private YayPipe game;
    private SplashScreen splashScreen;
    private MainMenuScreen mainMenuScreen;
    private GameSetupScreen gameSetUpScreen;
    private StatisticsScreen statisticsScreen;

    public ScreenManager(YayPipe game){
        this.game = game;
        splashScreen = new SplashScreen(game);
    }

    public SplashScreen getSplashScreen(){
        return splashScreen;
    }

    public MainMenuScreen getMainMenuScreen() {
        if (mainMenuScreen == null){
            mainMenuScreen = new MainMenuScreen(game);
        }
        return mainMenuScreen;
    }

    public GameSetupScreen getGameSetUpScreen() {
        if (gameSetUpScreen == null){
            gameSetUpScreen = new GameSetupScreen(game);
        }
        return gameSetUpScreen;
    }

    public StatisticsScreen getStatisticsScreen() {
        return statisticsScreen;
    }

    public void dispose(){
        if (splashScreen != null){
            splashScreen.dispose();
        }
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
