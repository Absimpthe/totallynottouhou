package com.tnt.game;

import com.badlogic.gdx.Game;

public class totallynottouhou extends Game {

	@Override
	public void create() {
		this.setScreen(new MainMenu(this)); // Initialize and set your MainMenu screen here
	}

	@Override
	public void render() {
		super.render(); // Important to call render() from the Game class to render the active screen
	}

	// Implement other necessary methods like dispose
	@Override
	public void dispose() {
		super.dispose(); // Cleanup resources if necessary
	}
}
