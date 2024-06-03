package com.fgdev.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.fgdev.game.Constants;

public class GamePreferences {

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();

    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int playerSkin;
    public boolean isGirl;
    public boolean showFpsCounter;
    public boolean useMonochromeShader;
    public boolean debug;

    private Preferences prefs;
    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load () {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f),
                0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f),
                0.0f, 1.0f);
        playerSkin = prefs.getInteger("playerSkin", 0);
        isGirl = prefs.getBoolean("isGirl", true);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);
        useMonochromeShader = prefs.getBoolean("useMonochromeShader",
                false);
        debug = prefs.getBoolean("debug", false);
    }
    public void save () {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("playerSkin", playerSkin);
        prefs.putBoolean("isGirl", isGirl);
        prefs.putBoolean("showFpsCounter", showFpsCounter);
        prefs.putBoolean("useMonochromeShader", useMonochromeShader);
        prefs.putBoolean("debug", debug);
        prefs.flush();
    }
}
