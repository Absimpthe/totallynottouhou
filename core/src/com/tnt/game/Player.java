package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private Sprite playerSprite;
    private Texture playerTexture;
    private Array<PlayerProjectile> projectiles;
    private Texture projectileTexture;
    private float shootTimer;
    private float shootInterval = 0.5f; // Time in seconds between shots
    private Sound shootingSound;

    public Player(String textureFileName) {
        this.playerTexture = new Texture(Gdx.files.internal(textureFileName));
        this.playerSprite = new Sprite(playerTexture);
        this.playerSprite.setScale(0.1f);
        this.playerSprite.setPosition(Gdx.graphics.getWidth() / 2f - playerSprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - playerSprite.getHeight() / 2);
        this.projectiles = new Array<>();
        this.shootTimer = 0;
        this.projectileTexture = new Texture(Gdx.files.internal("playerprojectile.png"));
        this.shootingSound = Gdx.audio.newSound(Gdx.files.internal("shootingsound.wav"));
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
                projectile.dispose(); // Dispose of the projectile's resources
            }
        }
    }
    private boolean isProjectileOffScreen(PlayerProjectile projectile) {
        // Assuming the projectile moves to the right, check if its position exceeds screen width
        return projectile.getPosition().x > Gdx.graphics.getWidth();
    }

    private void updatePlayerPosition() {
        // Get the mouse position
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert the Y-coordinate

        // Calculate the new position of the player sprite based on the mouse position
        // Ensuring the sprite's center is aligned with the mouse cursor
        float newX = mouseX - (playerSprite.getWidth() / 2);
        float newY = mouseY - (playerSprite.getHeight() / 2);

        // Constrain the newX position within the screen bounds
        // DO NOT CHANGE ANY OF THE VALUES HERE OR THE CONTROLS WILL BE MESSED UP
        if (newX < -500) {
            newX = -500; // Left edge
        } else if (newX > 650) {
            newX = 650; // Right edge
        }

        // Constrain the newY position within the screen bounds
        if (newY < -180) {
            newY = -180; // Bottom edge
        } else if (newY > 490) {
            newY = 490; // Top edge
        }

        // Set the new position of the player sprite
        playerSprite.setPosition(newX, newY);
    }

    private void shootProjectile() {
        // Adjust the starting X position. DO NOT CHANGE THE VALUES HERE OR THEY WILL SPAWN AT THE WRONG POSITION
        Vector2 projectilePosition = new Vector2(playerSprite.getX() + 500f + (playerSprite.getWidth() * playerSprite.getScaleX()),
                playerSprite.getY() + 172f + (playerSprite.getHeight() * playerSprite.getScaleY()) / 2);
        Vector2 projectileVelocity = new Vector2(400f, 0);
        PlayerProjectile newProjectile = new PlayerProjectile(projectilePosition, projectileVelocity, projectileTexture);
        projectiles.add(newProjectile);
        if (MainMenu.isSFXEnabled()) {
            shootingSound.play(0.1f);
        }
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
        shootingSound.dispose();
    }
}
