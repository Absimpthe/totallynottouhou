package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MainMenu implements Screen {
    private final aquamarine game;
    private static final int FRAME_COLS = 7, FRAME_ROWS = 11;
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Music mainmenuBGM;
    private Music shootingSound; 
    Animation<TextureRegion> animation;
    Texture sheet;
    float stateTime;
    SpriteBatch batch;

    // Toggle flags for music and sound effects
    private boolean isMusicOn = true;
    private boolean isSoundOn = true;

    public MainMenu(aquamarine game) {
        this.game = game;
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

        // Create a LabelStyle
        Label.LabelStyle labelStyle = new Label.LabelStyle();

        /*---------------
        Initialize the Label
        --------------- */
        labelStyle.font = skin.getFont("title");
        titleLabel = new Label("Aquamarine", labelStyle);
        titleLabel.setSize(500, 100);
        titleLabel.setPosition(Gdx.graphics.getWidth() / 2 - titleLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 160); // Position it above the start button
        titleLabel.setAlignment(Align.center); // Center the text
        stage.addActor(titleLabel); // Add the label to the stage

        /*---------------
        create "Start" button
        --------------- */
        TextButton startButton = new TextButton("Start", skin);
        startButton.setSize(350, 100); // Set the size of the button
        startButton.setPosition(Gdx.graphics.getWidth() / 2 - startButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 40); // Center the button
        startButton.setScale(1);  // Set the initial scale to 1 (100%)

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Sequence of actions: press down, release, then change screen
                startButton.addAction(Actions.sequence(
                    Actions.scaleTo(0.9f, 0.9f, 0.1f), // Simulate press down
                    Actions.scaleTo(1f, 1f, 0.1f),    // Simulate release
                    Actions.run(new Runnable() {      // Change screen
                        @Override
                        public void run() {
                            game.setScreen(new Level(game));
                        }
                    })
                ));
            }
        });
        stage.addActor(startButton); // Add the button to the stage

        /*---------------
        create "Instruction" button
        --------------- */
        TextButton instructionButton = new TextButton("How To Play", skin);
        instructionButton.setSize(350, 100); // Set the size of the button
        // Set the position below the Start button with some margin
        instructionButton.setPosition(Gdx.graphics.getWidth() / 2 - instructionButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 70);
        instructionButton.setScale(1);  // Set the initial scale to 1 (100%)
        instructionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Sequence of actions: press down, release, then change screen
                instructionButton.addAction(Actions.sequence(
                    Actions.scaleTo(0.9f, 0.9f, 0.1f), // Simulate press down
                    Actions.scaleTo(1f, 1f, 0.1f),    // Simulate release
                    Actions.run(new Runnable() {      // Change screen
                        @Override
                        public void run() {
                            // game.setScreen(new Level(game));
                        }
                    })
                ));
            }
        });
        stage.addActor(instructionButton);

        /*---------------
        Load and play the background music
        --------------- */
        mainmenuBGM = Gdx.audio.newMusic(Gdx.files.internal("mainmenubgm.mp3"));
        mainmenuBGM.setLooping(true);
        mainmenuBGM.setVolume(0.5f);
        mainmenuBGM.play();

        /*---------------
        Load and play the sound effect
        --------------- */
        // source: https://pixabay.com/sound-effects/search/gun/
        shootingSound = Gdx.audio.newMusic(Gdx.files.internal("shootingsound.mp3"));
        shootingSound.setLooping(true);
        shootingSound.setVolume(0.5f);
        shootingSound.play();

        /*---------------
        Create and position music button
        --------------- */
        TextButton musicButton = new TextButton(isMusicOn ? "Music: On" : "Music: Off", skin);
        musicButton.setSize(350, 100);
        musicButton.setPosition(Gdx.graphics.getWidth() / 2 - musicButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 180);
        // musicButton.setScale(0.2f, 0.2f); 
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMusicOn = !isMusicOn; // Toggle music state
                musicButton.setText(isMusicOn ? "Music On" : "Music Off");
                if (isMusicOn) {
                    mainmenuBGM.play(); // Resume music
                } else {
                    mainmenuBGM.pause(); // Pause music
                }
            }
        });
        stage.addActor(musicButton);

        /*---------------
        Create and position sound effect buttons
        --------------- */
        TextButton soundButton = new TextButton(isSoundOn ? "Sound: On" : "Sound: Off", skin);
        soundButton.setSize(350,100);
        soundButton.setPosition(Gdx.graphics.getWidth() / 2 - soundButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 290);
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSoundOn = !isSoundOn; // Toggle sound state
                soundButton.setText(isSoundOn ? "Sound On" : "Sound Off");
                if (isSoundOn) {
                    shootingSound.play(); // Resume sound effect
                } else {
                    shootingSound.pause(); // Pause sound effect
                }
            }
        });
        stage.addActor(soundButton);
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
        mainmenuBGM.stop();
    }
    @Override
    public void dispose() {
        // Dispose of assets when done
        if (batch != null) batch.dispose();
        if (sheet != null) sheet.dispose();
        if (stage != null) stage.dispose();
        if (mainmenuBGM != null) mainmenuBGM.dispose();
    }
}

