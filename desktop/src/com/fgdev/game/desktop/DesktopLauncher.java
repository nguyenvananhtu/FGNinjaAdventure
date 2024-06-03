package com.fgdev.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fgdev.game.FGDevMain;

public class DesktopLauncher {

	private static final int WIDTH = 1240;
	private static final int HEIGHT = 630;

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(WIDTH, HEIGHT);
		new Lwjgl3Application(new FGDevMain(), config);
	}
}
