package com.tnt.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Level implements Screen {
    private final aquamarine game;
    private SpriteBatch batch;
    private Texture characterTexture;
    private Sprite characterSprite;
    private Texture bgtexture;
    Animation<TextureRegion> animation;
    private static final int FRAME_COLS = 7, FRAME_ROWS = 11;
    float stateTime;
    public Level(aquamarine game) {
        this.game = game;
        bgtexture = new Texture(Gdx.files.internal("mainmenubgframes.png"));
        TextureRegion[][] tmp = TextureRegion.split(bgtexture,
                bgtexture.getWidth() / FRAME_COLS,
                bgtexture.getHeight() / FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_COLS];
        for (int j = 0; j < FRAME_COLS; j++) {
            frames[j] = tmp[FRAME_ROWS - 1][j]; // Use FRAME_ROWS - 1 to access the last row
        }
        animation = new Animation<TextureRegion>(0.1f, frames);

        stateTime = 0f;
        // Load the sprite
        batch = new SpriteBatch();
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        float scaleX = (float)Gdx.graphics.getWidth() / currentFrame.getRegionWidth();
        float scaleY = (float)Gdx.graphics.getHeight() / currentFrame.getRegionHeight();
        // Use the larger scale factor to ensure the background covers the entire window
        float scale = Math.max(scaleX, scaleY);

        // Calculate the width and height to draw the texture region at the scaled size
        float drawWidth = currentFrame.getRegionWidth() * scale;
        float drawHeight = currentFrame.getRegionHeight() * scale;
        // Center the background
        float drawX = (Gdx.graphics.getWidth() - drawWidth) / 2;
        float drawY = (Gdx.graphics.getHeight() - drawHeight) / 2;

        batch.begin();
        // Draw the sprite
        batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);
        characterSprite.draw(batch);
        batch.end();
        updateSpritePosition();
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