package com.tnt.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Player {
    private Sprite playerSprite;
    private Texture playerTexture;
    private Array<PlayerProjectile> projectiles;
    private Texture projectileTexture;
    private float shootTimer;
    private float shootInterval = 0.25f; // Time in seconds between shots

    public Player(String textureFileName) {
        this.playerTexture = new Texture(Gdx.files.internal(textureFileName));
        this.playerSprite = new Sprite(playerTexture);
        this.playerSprite.setScale(0.1f);
        this.playerSprite.setPosition(Gdx.graphics.getWidth() / 2f - playerSprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - playerSprite.getHeight() / 2);
        this.projectiles = new Array<>();
        this.shootTimer = 0;
        this.projectileTexture = new Texture(Gdx.files.internal("playerprojectile.png"));
    }

    public void update(float deltaTime) {
        updatePlayerPosition();
        // Update shoot timer and shoot if it's time
        shootTimer += deltaTime;
        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            shootProjectile();
        }

        // Iterate through projectiles and update their positions
        for (int i = projectiles.size - 1; i >= 0; i--) {
            PlayerProjectile projectile = projectiles.get(i);
            projectile.updateProjectile(deltaTime);

            // Check if the projectile has gone off-screen
            if (isProjectileOffScreen(projectile)) {
                projectiles.removeIndex(i); // Remove the projectile from the array
                projectile.dispose(); // Optionally dispose of the projectile's resources
            }
        }
    }
    private boolean isProjectileOffScreen(PlayerProjectile projectile) {
        // Assuming the projectile moves to the right, check if its position exceeds screen width
        return projectile.getPosition().x > Gdx.graphics.getWidth();
    }

    private void updatePlayerPosition() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert the Y-coordinate
        playerSprite.setPosition(mouseX - playerSprite.getWidth() / 2, mouseY - playerSprite.getHeight() / 2);
    }

    private void shootProjectile() {
        // Adjust the starting X position. DO NOT CHANGE THE VALUES HERE OR THEY WILL SPAWN AT THE WRONG POSITION
        Vector2 projectilePosition = new Vector2(playerSprite.getX() + 500f + (playerSprite.getWidth() * playerSprite.getScaleX()),
                playerSprite.getY() + 171f + (playerSprite.getHeight() * playerSprite.getScaleY()) / 2);
        Vector2 projectileVelocity = new Vector2(400f, 0);
        PlayerProjectile newProjectile = new PlayerProjectile(projectilePosition, projectileVelocity, projectileTexture);
        projectiles.add(newProjectile);
    }
    public void draw(SpriteBatch batch) {
        playerSprite.draw(batch);
        for (PlayerProjectile projectile : projectiles) {
            projectile.draw(batch);
        }
    }

    public void dispose() {
        playerTexture.dispose();
        projectileTexture.dispose();
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
