package com.fgdev.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.fgdev.game.Constants;

public class ValueManager {

    public static final String TAG = ValueManager.class.getName();

    public static final ValueManager instance = new ValueManager();
    public String mapPath;
    public int score;
    public int lives;
    public float livesVisual;
    public float scoreVisual;
    public float timeLeftGameOverDelay;
    public float timeLeftLiveLost;
    public float timeNextLevel;
    public float posX;
    public float posY;
    public Texture background;
    public boolean isNextLevel;
    public int levelCurrent;
    public int totalLevel;

    // singleton: prevent instantiation from other classes
    private ValueManager() { }

    public void init() {
        mapPath = Constants.LEVEL_01;
        lives = Constants.LIVES_START;
        livesVisual = lives;
        score = 0;
        scoreVisual = score;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
        timeNextLevel = Constants.TIME_NEXT_LEVEL;
        timeLeftLiveLost = 0;
        posX = Constants.POS_X;
        posY = Constants.POS_Y;
        background = Assets.instance.textures.background1;
        isNextLevel = false;
        levelCurrent = 1;
        totalLevel = Constants.TOTAL_LEVEL;
    }

    public boolean isGameOver () {
        return lives < 0;
    }
}
