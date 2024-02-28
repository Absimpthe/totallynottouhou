package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenu implements Screen {

    private static final int FRAME_COLS = 7, FRAME_ROWS = 11;

    Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
    Texture sheet;
    float stateTime;
    SpriteBatch batch;
    public MainMenu(totallynottouhou game) {
        // Load the sprite sheet as a Texture
        sheet = new Texture(Gdx.files.internal("mainmenubgframes.png"));

        // Split the sprite sheet into individual frames
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / FRAME_COLS,
                sheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order
        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        animation = new Animation<TextureRegion>(0.025f, frames);

        stateTime = 0f;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
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
        batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);
        batch.end();
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
    // Implement other required methods with empty bodies
    @Override
    public void dispose() {
        // Dispose of assets when done
        if (batch != null) batch.dispose(); // Dispose of the SpriteBatch
        if (sheet != null) sheet.dispose(); // Dispose of the Texture
    }
}
