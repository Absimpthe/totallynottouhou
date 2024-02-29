package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenu implements Screen {

    private static final int FRAME_COLS = 7, FRAME_ROWS = 11;
    private Stage stage;
    private Skin skin;
    private Label titleLabel;

    Animation<TextureRegion> animation;
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
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Set input processor
        skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));
        TextButton startButton = new TextButton("Start", skin);
        startButton.setSize(200, 100); // Set the size of the button
        startButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 50); // Center the button
        // Add a ClickListener to the button
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the button click
            }
        });
        stage.addActor(startButton); // Add the button to the stage

        TextButton volumeButton = new TextButton("Volume", skin);
        volumeButton.setSize(200, 100); // Set the size of the button
        // Set the position below the Start button with some margin
        volumeButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 160);
        volumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the button click
            }
        });
        stage.addActor(volumeButton);

        // Create a LabelStyle
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("title");
        // Initialize the Label
        titleLabel = new Label("Aquamarine", labelStyle);
        titleLabel.setSize(300, 100);
        titleLabel.setPosition(Gdx.graphics.getWidth() / 2 - titleLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100); // Position it above the start button
        titleLabel.setAlignment(Align.center); // Center the text
        stage.addActor(titleLabel); // Add the label to the stage
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
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw(); // Draw the stage and its actors
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
        // Dispose of assets when done
        if (batch != null) batch.dispose();
        if (sheet != null) sheet.dispose();
        if (stage != null) stage.dispose();
    }
}
