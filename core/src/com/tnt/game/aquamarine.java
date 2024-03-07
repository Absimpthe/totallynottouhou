package com.tnt.game;

import com.badlogic.gdx.Game;

public class aquamarine extends Game {
	@Override
	public void create() {
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
