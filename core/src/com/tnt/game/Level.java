package com.tnt.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Level implements Screen {
    private aquamarine game;
    private SpriteBatch batch;
    private Texture characterTexture;
    private Sprite characterSprite;

    public Level(aquamarine game) {
        this.game = game;
        batch = new SpriteBatch();
        // Load the sprite
        characterTexture = new Texture(Gdx.files.internal("submarine.png"));
        characterSprite = new Sprite(characterTexture);
        characterSprite.setScale(0.1f);
        characterSprite.setPosition(Gdx.graphics.getWidth() / 2f - characterSprite.getWidth() / 2, Gdx.graphics.getHeight() / 2f - characterSprite.getHeight() / 2);
    }
    @Override
    public void show() {

    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateSpritePosition();
        batch.begin();
        // Draw the sprite
        characterSprite.draw(batch);
        batch.end();
    }

    private void updateSpritePosition() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert the Y-coordinate

        // Update the sprite's position
        characterSprite.setPosition(mouseX - characterSprite.getWidth() / 2, mouseY - characterSprite.getHeight() / 2);
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
        if (characterTexture != null) characterTexture.dispose();
    }
}