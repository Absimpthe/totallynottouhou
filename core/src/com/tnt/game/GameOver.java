package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOver implements Screen {
    private Stage stage;
    private final aquamarine game;
    private final Skin skin;
    private int finalScore;

    public GameOver(aquamarine game) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));
        // this.finalScore = finalScore;
    }
    
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        Label gameOverLabel = new Label("Game Over", skin, "title");
        // Position the Game Over label a bit higher than the center
        gameOverLabel.setPosition(Gdx.graphics.getWidth() / 2 - gameOverLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gameOverLabel.getHeight() + 20);
        
        // Label scoreLabel = new Label("Score: " + finalScore, skin);
        // scoreLabel.setPosition(Gdx.graphics.getWidth() / 2 - scoreLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2 + scoreLabel.getHeight() - 20);

        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.setSize(600, 100);
        // Position the restart button a bit lower than the Game Over label
        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - restartButton.getWidth() / 2, gameOverLabel.getY() - restartButton.getHeight() - 80); // 20 pixels gap from the label
        
        TextButton backToMenuButton = new TextButton("Back To Menu", skin);
        backToMenuButton.setSize(600, 100);
        // Position the Back To Menu button a bit lower than the Restart button
        backToMenuButton.setPosition(Gdx.graphics.getWidth() / 2 - backToMenuButton.getWidth() / 2, restartButton.getY() - backToMenuButton.getHeight() - 50); // 20 pixels gap from the restart button
        
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Restart the game
                game.setScreen(new Level(game));
            }
        });

        backToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Restart the game
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(gameOverLabel);
        stage.addActor(restartButton);
        stage.addActor(backToMenuButton);

        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
