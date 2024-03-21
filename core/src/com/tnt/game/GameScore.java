package com.tnt.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScore {
    private int score = 0; // Initialize score to 0
    private Label scoreLabel; // Label to display the score

    public GameScore(Stage stage, Skin skin) {
        // Create a Label using the skin and set its initial text to "Score: 0"
        scoreLabel = new Label("Score: 0", skin);

        // Set the position of the score label (adjust as needed)
        // Assuming you want to place it under the heart images which might be around y=660
        scoreLabel.setPosition(10, 620); // Adjust x, y to fit under your heart images

        // Add the score label to the stage so it's rendered and managed by the stage
        stage.addActor(scoreLabel);
    }

    public void addScore(int value) {
        // Increase the score by the specified value
        score += value;

        // Update the text of the score label to reflect the new score
        scoreLabel.setText("Score: " + score);
    }

    public int getScore() {
        // In case you need to access the current score value
        return score;
    }
}
