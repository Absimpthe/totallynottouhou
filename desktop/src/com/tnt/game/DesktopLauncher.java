package com.tnt.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Set the window size
		config.setWindowedMode(1280, 720); // Example size, change as needed

		// Disable window resizing
		config.setResizable(false);
		config.setTitle("Aquamarine");
		// Create and start the application
		new Lwjgl3Application(new totallynottouhou(), config); // Replace 'MainClass' with your game's main class
	}
}
