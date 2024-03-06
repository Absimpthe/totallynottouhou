package com.tnt.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class Player {
    private Sprite playerSprite;
    private Texture playerTexture;

    public Player(String textureFileName) {
        this.playerTexture = new Texture(Gdx.files.internal(textureFileName));
        this.playerSprite = new Sprite(playerTexture);
        this.playerSprite.setScale(0.1f);
        this.playerSprite.setPosition(Gdx.graphics.getWidth() / 2f - playerSprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - playerSprite.getHeight() / 2);
    }

    public void update(float deltaTime) {
        updatePlayerPosition();
    }

    private void updatePlayerPosition() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert the Y-coordinate
        playerSprite.setPosition(mouseX - playerSprite.getWidth() / 2, mouseY - playerSprite.getHeight() / 2);
    }

    public void draw(SpriteBatch batch) {
        playerSprite.draw(batch);
    }

    public void dispose() {
        playerTexture.dispose();
    }

    // Getter for player's position if needed by the projectile manager
    public Vector2 getPosition() {
        return new Vector2(playerSprite.getX(), playerSprite.getY());
    }

    // Getter for player's sprite width and height if needed
    public float getWidth() {
        return playerSprite.getWidth();
    }

    public float getHeight() {
        return playerSprite.getHeight();
    }
}
