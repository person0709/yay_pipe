package com.lazybean.yaypipe.gamehelper;

public interface PlayService {
    String SKU_REMOVE_ADS = "test_product_1";
    int RC_REQUEST = 10001;

    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(AchievementType achievementType);
    void incrementAchievement(AchievementType achievementType, int count);
    void submitScore(int highScore);
    void showAchievement();
    void showLeaderBoards();
    boolean isSignedIn();

    void removeAds();
}
