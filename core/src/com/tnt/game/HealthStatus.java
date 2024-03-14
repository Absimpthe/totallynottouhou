package com.tnt.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class HealthStatus {
    private final aquamarine game;
    private ProgressBar healthBar;

    public HealthStatus(aquamarine game, Skin skin) {
        this.game = game;
        // Retrieve the ProgressBarStyle from the skin
        ProgressBarStyle progressBarStyle = skin.get("health", ProgressBarStyle.class);

        // Create a new ProgressBar with the style, min value, max value, and step size
        healthBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
        healthBar.setValue(100); 
        healthBar.setSize(500, 50); // Example size
        healthBar.setPosition(10,640); 
    }

    public void update(float delta) {
        // Here you would update the health bar's value based on the game's status
    }

    public void addToStage(Stage stage) {
        // Add health bar to the stage
        stage.addActor(healthBar);
    }
}
