package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Level implements Screen {
    private final aquamarine game;
    private SpriteBatch batch;
    private SpriteBatch bgbatch;
    private Music levelbgm;
    private Parallax ParallaxBG;
    private Player player;
    public Level(aquamarine game) {
        this.game = game;
        // Load the sprite
        batch = new SpriteBatch();
        // Initialize the player
        player = new Player("submarine.png");
    }
    @Override
    public void show() {
        // Play background music
        levelbgm = Gdx.audio.newMusic(Gdx.files.internal("levelBGM.mp3"));
        levelbgm.setLooping(true);
        levelbgm.setVolume(0.5f);
        levelbgm.play();
        // Initialize parallax background
        bgbatch = new SpriteBatch();
        ParallaxBG = new Parallax();
        ParallaxBG.addLayer(new Texture("far.png"), 30f); // Farthest layer
        ParallaxBG.addLayer(new Texture("sand.png"), 60f); // Middle layer
        ParallaxBG.addLayer(new Texture("foreground-merged.png"), 90f);   // Closest layer
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ParallaxBG.update(delta);
        bgbatch.begin();
        ParallaxBG.render(bgbatch);
        bgbatch.end();
        // Draw the sprite
        batch.begin();
        player.draw(batch);
        batch.end();
        player.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (player != null) player.dispose();
    }
}