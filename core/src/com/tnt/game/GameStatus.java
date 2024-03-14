package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameStatus {
    private final aquamarine game; // Ensure Aquamarine is correctly named and exists
    private Stage stage;
    private ImageButton settingsButton;

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
        
                optionsDialog.button("Back to Menu", "back");
                optionsDialog.button("Restart", "restart");
                optionsDialog.button("Start Over", "startOver");
        
                optionsDialog.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Implement screen transitions here based on the button pressed
                        optionsDialog.hide();
                    }
                });
        
                optionsDialog.show(stage);
            }
        });        
    }

    public void addToStage(Stage stage) {
        this.stage = stage; // Save the stage reference
        stage.addActor(settingsButton); // Add the settings button to the stage
    }
}
