package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;

public class GameStatus {
    private final aquamarine game; // Ensure Aquamarine is correctly named and exists
    private Stage stage;
    private ImageButton settingsButton;
    private Music levelbgm;
    
    public void setLevelBGM(Music levelbgm) {
        this.levelbgm = levelbgm;
    }

    public GameStatus(aquamarine game, Skin skin) {
        this.game = game;
        Texture settingsTexture = new Texture(Gdx.files.internal("settingsicon.png"));
        Drawable settingsDrawable = new TextureRegionDrawable(new TextureRegion(settingsTexture));
        settingsButton = new ImageButton(settingsDrawable); // Removed local declaration
        settingsButton.setSize(50, 50); // Set size of button (in pixels)
        settingsButton.setPosition(Gdx.graphics.getWidth() - settingsButton.getWidth() - 10, 10); // Position in bottom-right
        
        settingsButton.addListener(new ClickListener() {
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog optionsDialog = new Dialog("", skin);
                optionsDialog.text("Choose an option");


                // -------------------------------
                // Add the "Resume" button 
                // -------------------------------
                TextButton resumeButton = new TextButton("Resume", skin);
                optionsDialog.getButtonTable().add(resumeButton).size(370, 95).pad(10).padTop(0).padBottom(20).row(); // Adjust the size as needed
                resumeButton.setPosition(200, 200);
                resumeButton.addListener(new ClickListener() {
            });

                // -------------------------------
                // Add the "Start Over" button 
                // -------------------------------
                TextButton startOverButton = new TextButton("Start Over", skin);
                optionsDialog.getButtonTable().add(startOverButton).size(370, 95).pad(10).padTop(10).padBottom(20).row(); // Adjust the size as needed
                startOverButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Sequence of actions: press down, release, then change screen
                        startOverButton.addAction(Actions.sequence(
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
                                        float initialVolume = levelbgm.getVolume(); // Assuming 'backgroundMusic' is your music instance
        
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
                                                levelbgm.setVolume(newVolume);
        
                                                if (alpha >= 1 || newVolume <= 0) {
                                                    this.cancel(); // Cancel the timer task when the fade-out is complete or volume reaches 0
                                                    levelbgm.stop();
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

                                // -------------------------------
                // Add the "Back to Menu" button 
                // -------------------------------
                TextButton backToMenuButton = new TextButton("Back to Menu", skin);
                optionsDialog.getButtonTable().add(backToMenuButton).size(370, 95).pad(10).padTop(10).padBottom(20).row(); // Adjust the size as needed
                backToMenuButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Sequence of actions: press down, release, then change screen
                        backToMenuButton.addAction(Actions.sequence(
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
                                        float initialVolume = levelbgm.getVolume(); // Assuming 'backgroundMusic' is your music instance
        
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
                                                levelbgm.setVolume(newVolume);
        
                                                if (alpha >= 1 || newVolume <= 0) {
                                                    this.cancel(); // Cancel the timer task when the fade-out is complete or volume reaches 0
                                                    levelbgm.stop();
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
                                                        game.setScreen(new MainMenu(game)); // Change to the level screen after the fade is complete
                                                    }
                                                })
                                        ));
                                    }
                                })
                        ));
                    }
                });
        
                optionsDialog.pack();
                
                optionsDialog.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Implement screen transitions here based on the button pressed
                        optionsDialog.hide();
                    }
                });
                optionsDialog.show(stage).pad(0);;
                optionsDialog.pack(); // Size the dialog based on its contents
                // Center the dialog
                float posX = (Gdx.graphics.getWidth() - optionsDialog.getWidth()) / 2;
                float posY = (Gdx.graphics.getHeight() - optionsDialog.getHeight()) / 2;
                optionsDialog.setPosition(posX, posY - 50);
                // optionsDialog.setPosition(posX , posY );
                optionsDialog.setSize(420, 500);
            }
        });        
    }

    public void addToStage(Stage stage) {
        this.stage = stage; // Save the stage reference
        stage.addActor(settingsButton); // Add the settings button to the stage
    }
}
