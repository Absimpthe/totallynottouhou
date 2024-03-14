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

    public GameOver(aquamarine game) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        Label gameOverLabel = new Label("Game Over", skin, "title");

        gameOverLabel.setPosition(Gdx.graphics.getWidth() / 2 - gameOverLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        TextButton restartButton = new TextButton("Restart", skin);

        restartButton.setPosition(Gdx.graphics.getWidth() / 2 - restartButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - restartButton.getHeight() * 2);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Restart the game
                game.setScreen(new Level(game));
            }
        });

        stage.addActor(gameOverLabel);
        stage.addActor(restartButton);

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
