package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

public class MainMenu implements Screen {
    private final aquamarine game;
    private Stage stage;
    private Skin skin;
    private Label titleLabel;
    private Music mainmenuBGM;
    private Parallax ParallaxBG;
    private SpriteBatch bgbatch;
    Texture sheet;
    SpriteBatch batch;

    // Toggle flags for music and sound effects
    public static boolean isSoundOn = true;
    Dialog instructionsDialog;

    public MainMenu(aquamarine game) {
        this.game = game;
    }

    @Override
    public void show() {
        bgbatch = new SpriteBatch();
        ParallaxBG = new Parallax();
        ParallaxBG.addLayer(new Texture("far.png"), 30f); // Farthest layer
        ParallaxBG.addLayer(new Texture("sand.png"), 60f); // Middle layer
        ParallaxBG.addLayer(new Texture("foreground-merged.png"), 90f);   // Closest layer

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
        TextButton startButton = new TextButton("Start", skin,"toggle");
        startButton.setSize(350, 100); // Set the size of the button
        startButton.setPosition(Gdx.graphics.getWidth() / 2 - startButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 40); // Center the button
        startButton.setScale(1);  // Set the initial scale to 1 (100%)

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Sequence of actions: press down, release, then change screen
                startButton.addAction(Actions.sequence(
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                // Create a full-screen black image actor to cover the screen
                                final Image blackOverlay = new Image(skin.newDrawable("white", Color.BLACK));
                                blackOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                                blackOverlay.getColor().a = 0f; // Start fully transparent
                                // Add the overlay to the stage
                                stage.addActor(blackOverlay);
                                final float fadeDuration = 1f; // Duration for the fade effect
                                float initialVolume = mainmenuBGM.getVolume(); // Assuming 'backgroundMusic' is your music instance

                                // Schedule a task to decrease the volume over time
                                Timer.schedule(new Timer.Task() {
                                    float elapsedTime = 0f;

                                    @Override
                                    public void run() {
                                        elapsedTime += Gdx.graphics.getDeltaTime(); // Increment elapsed time
                                        float alpha = elapsedTime / fadeDuration; // Calculate the fade-out progress
                                        // Calculate the new volume
                                        float newVolume = Math.max(initialVolume * (1 - alpha), 0);

                                        // Set the volume to the new value or 0 if the new value is negative
                                        mainmenuBGM.setVolume(newVolume);

                                        if (alpha >= 1 || newVolume <= 0) {
                                            this.cancel(); // Cancel the timer task when the fade-out is complete or volume reaches 0
                                            mainmenuBGM.stop();
                                        }
                                    }
                                }, 0, 1/60f, (int) (fadeDuration * 60)); // Schedule the task to run every frame (1/60 second)

                                // Use a FadeIn action for the blackOverlay
                                AlphaAction fadeOutAction = Actions.fadeIn(fadeDuration);
                                blackOverlay.addAction(fadeOutAction);

                                // Fade the overlay to full opacity
                                blackOverlay.addAction(Actions.sequence(
                                        Actions.fadeIn(0.75f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                game.setScreen(new Level(game)); // Change to the level screen after the fade is complete
                                            }
                                        })
                                ));
                            }
                        })
                ));
            }
        });
        stage.addActor(startButton); // Add the button to the stage

        /*---------------
        create "How To Play" button
        --------------- */
        TextButton instructionButton = new TextButton("How To Play", skin);
        instructionButton.setSize(350, 100); // Set the size of the button
        // Set the position below the Start button with some margin
        instructionButton.setPosition(Gdx.graphics.getWidth() / 2 - instructionButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 70);
        instructionButton.setScale(1);  // Set the initial scale to 1 (100%)

        // Step 2: Initialize and configure the Dialog
        instructionsDialog = new Dialog("", skin); // Removed title here

        // Configure title label
        Label titleLabel = new Label("How To Play", skin, "subtitle"); // Assuming "title" is your style name
        titleLabel.setAlignment(Align.center); // Align title text to center
        // Add title label to the title table of the dialog
        instructionsDialog.getTitleTable().clearChildren(); // Clear any existing content first
        instructionsDialog.getTitleTable().add(titleLabel).padTop(60).expandX().fillX(); // Adjust padTop as needed for position

        // Configure content label
        Label instructionsLabel = new Label("The merfolk don't like it when humans intrude in their territory.\n\n Move the mouse to dodge their attacks, and try to survive as long as possible...", skin);
        instructionsLabel.setWrap(true); // Enable word-wrap
        instructionsLabel.setAlignment(Align.center); // Align text to center
        instructionsDialog.getContentTable().add(instructionsLabel).width(680).padTop(70).center();

        instructionsDialog.pack(); // Automatically size the dialog based on its contents
        instructionsDialog.setSize(Math.max(instructionsDialog.getWidth(), 700), Math.max(instructionsDialog.getHeight(), 200)); // Ensure the dialog is not smaller than desired
        instructionsDialog.setPosition(Gdx.graphics.getWidth() / 2 - instructionsDialog.getWidth() / 2, Gdx.graphics.getHeight() / 2 - instructionsDialog.getHeight() / 2); // Center the dialog on screen

        // Add the 'OK' button at the bottom right of the dialog
        TextButton okButton = new TextButton("OK", skin);
        instructionsDialog.getButtonTable().add(okButton).size(120, 85).bottom().right().padBottom(5).padRight(10); // Adjust the size as needed
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionsDialog.hide();
            }
        });

        // Set the button listener for the dialog
        instructionsDialog.getButtonTable().getCells().first().getActor().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionsDialog.hide();
            }
        });

        instructionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                instructionsDialog.show(stage);
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
        Create and position music button
        --------------- */
        isSoundOn = true;
        TextButton soundButton = new TextButton(isSoundOn ? "Sound: On" : "Sound: Off", skin);
        soundButton.setSize(350, 100);
        soundButton.setPosition(Gdx.graphics.getWidth() / 2 - soundButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 180);
        // musicButton.setScale(0.2f, 0.2f); 
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isSoundOn = !isSoundOn; // Toggle music state
                soundButton.setText(isSoundOn ? "Sound: On" : "Sound: Off");
                if (isSoundOn) {
                    mainmenuBGM.play(); // Resume music
                } else {
                    mainmenuBGM.pause(); // Pause music
                }
            }
        });
        stage.addActor(soundButton);
        
        /*---------------
        Create and position sound effect buttons
        --------------- */
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.setSize(350,100);
        quitButton.setPosition(Gdx.graphics.getWidth() / 2 - quitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 290);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(quitButton);
    }

    public static boolean isSFXEnabled() {
        return isSoundOn;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ParallaxBG.update(delta);
        bgbatch.begin();
        ParallaxBG.render(bgbatch);
        bgbatch.end();
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
        if (bgbatch != null) bgbatch.dispose();
    }
}

