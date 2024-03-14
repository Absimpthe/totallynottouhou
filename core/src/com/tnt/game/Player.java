package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private final ShapeRenderer shapeRenderer; // ShapeRenderer is used for debugging. DO NOT DELETE YET
    private final Sprite playerSprite;
    private final Texture playerTexture;
    private Array<PlayerProjectile> projectiles;
    private Texture projectileTexture;
    private float shootTimer;
    private final float shootInterval = 0.5f; // Time in seconds between shots
    private final Sound shootingSound;
    private final Rectangle playerbounds;
    private float currentHp;
    private final float maxHp;
    private Animation<TextureRegion> explosionAnimation;
    private boolean isExploding;
    private float explosionTimer;
    private static final float EXPLOSION_FRAME_DURATION = 0.1f; // Adjust the frame duration as needed
    private boolean isVisible;
    private final Music explosionSound; // SFX initialized as music to ensure it only plays once (sounds can overlap, music cannot)

    public Player(String textureFileName) {
        this.isVisible = true;
        this.playerTexture = new Texture(Gdx.files.internal(textureFileName));
        this.playerSprite = new Sprite(playerTexture);
        this.playerSprite.setScale(0.1f);
        this.playerSprite.setPosition(Gdx.graphics.getWidth() / 2f - playerSprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2f - playerSprite.getHeight() / 2);
        this.projectiles = new Array<>();
        this.shootTimer = 0;
        this.projectileTexture = new Texture(Gdx.files.internal("playerprojectile.png"));
        this.shootingSound = Gdx.audio.newSound(Gdx.files.internal("shootingsound.wav"));
        this.playerbounds = new Rectangle(playerSprite.getX(), playerSprite.getY(),
                playerSprite.getWidth() * playerSprite.getScaleX(),
                playerSprite.getHeight() * playerSprite.getScaleY());
        this.maxHp = 300f;
        this.currentHp = maxHp;
        this.explosionSound = Gdx.audio.newMusic(Gdx.files.internal("explosionSound.wav"));

        // Shape renderer used for debugging
        this.shapeRenderer = new ShapeRenderer();
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
        if (isExploding) {
            isVisible = false; // Make the player sprite disappear. Better than disposing of it entirely in case the player restarts the level
            explosionTimer += deltaTime;
            if (explosionAnimation.isAnimationFinished(explosionTimer)) {
                isExploding = false;
                // Trigger game over or respawn logic here
            }
        }
    }
    private boolean isProjectileOffScreen(PlayerProjectile projectile) {
        // Assuming the projectile moves to the right, check if its position exceeds screen width
        return projectile.getPosition().x > Gdx.graphics.getWidth();
    }

    public boolean checkCollision(Rectangle otherBounds) {
        return playerbounds.overlaps(otherBounds); // Check if this projectile's bounds overlap with another object's bounds
    }

    public void takeDamage(float damage) {
        currentHp -= damage; // Decrease HP by damage taken
        if (currentHp <= 0) {
            currentHp = 0; // Ensure HP doesn't go below 0
            if (MainMenu.isSFXEnabled()) {
                explosionSound.play();
            }
            onPlayerDeath();
        }
    }

    private void onPlayerDeath() {
        isExploding = true;
        Texture explosionSheet = new Texture(Gdx.files.internal("deathexplosion.png"));
// Assuming the sprite sheet is a grid of frames with equal size
        int frameCols = 6; // Number of columns in the sprite sheet
        int frameRows = 5; // Number of rows in the sprite sheet
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet,
                explosionSheet.getWidth() / frameCols,
                explosionSheet.getHeight() / frameRows);
        TextureRegion[] explosionFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                explosionFrames[index++] = tmp[i][j];
            }
        }
        explosionAnimation = new Animation<TextureRegion>(EXPLOSION_FRAME_DURATION, explosionFrames);
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
        // Update player's hitbox. DO NOT CHANGE THESE VALUES OR THE HITBOX WILL BE IN THE WRONG POSITION
        playerbounds.setPosition(playerSprite.getX() + 504f, playerSprite.getY() + 185f);
    }

    // Method to highlight the position of the hitbox. Used only for debugging, DO NOT DELETE YET
    public void drawHitbox() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(playerbounds.x, playerbounds.y, playerbounds.width, playerbounds.height);
        shapeRenderer.end();
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
        if (isVisible) {
            playerSprite.draw(batch);
            for (PlayerProjectile projectile : projectiles) {
                projectile.draw(batch);
            }
        }
        if (isExploding) {
            batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
                    playerSprite.getX() + 380f, playerSprite.getY() + 25f);
        }
    }

    public void dispose() {
        playerTexture.dispose();
        projectileTexture.dispose();
        shootingSound.dispose();
        explosionSound.dispose();
    }
}
