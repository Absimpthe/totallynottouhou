package com.tnt.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScore {
    private int score = 0; // Initialize score to 0
    private Label scoreLabel; // Label to display the score
    private Stage stage;

    public GameScore(Stage stage, Skin skin) {
        // Create a Label using the skin and set its initial text to "Score: 0"
        scoreLabel = new Label("Score: 0", skin);

        // Set the position of the score label (adjust as needed)
        // Assuming you want to place it under the heart images which might be around y = 660
        scoreLabel.setPosition(10, 620); // Adjust x, y to fit under your heart images
    }

    public void addScore(int value) {
        // Increase the score by the specified value
        score += value;

        // Update the text of the score label to reflect the new score
        scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        return score;
    }

    public void addToStage(Stage stage) {
        this.stage = stage; // Save the stage reference
        stage.addActor(scoreLabel); // Add the scoreLabel to the stage
    }
}
