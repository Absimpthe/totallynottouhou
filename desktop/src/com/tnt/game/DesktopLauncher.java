package com.tnt.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Set the window size
		config.setWindowedMode(1280, 720);
		// Disable window resizing
		config.setResizable(false);
		config.setTitle("Aquamarine");
		config.useVsync(true);
		config.setForegroundFPS(60);
		// Create and start the application
		new Lwjgl3Application(new aquamarine(), config);
	}
}