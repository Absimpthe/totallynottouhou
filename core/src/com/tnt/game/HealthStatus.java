package com.tnt.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthStatus {
    private final aquamarine game;
    private Stage stage; // Assume there's a Stage available
    private Texture heartTexture;
    private Image[] heartImages;
    private static final int LIVES = 3; // Number of lives

    public HealthStatus(aquamarine game, Stage stage) {
        this.game = game;
        this.stage = stage;
        heartTexture = new Texture(Gdx.files.internal("heart.png"));
        heartImages = new Image[LIVES];

        for (int i = 0; i < LIVES; i++) {
            Drawable heartDrawable = new TextureRegionDrawable(new TextureRegion(heartTexture));
            heartImages[i] = new Image(heartDrawable);
            heartImages[i].setSize(50, 50); // Set the size
            heartImages[i].setPosition(10 + i * 60, 660); // Set the position
            stage.addActor(heartImages[i]); // Add to stage
        }
    }

    public void updateHearts(int currentLives) {
    // Hide or remove hearts based on currentLives
    for (int i = 0; i < heartImages.length; i++) {
        if (i < currentLives) {
            heartImages[i].setVisible(true);
        } else {
            heartImages[i].setVisible(false);
        }
        }
    }
}
