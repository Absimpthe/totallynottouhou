package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class EnemyMermaid {
    public Animation<TextureRegion> mermaidAnimation; // Animation instead of Sprite
    private float stateTime; // Track elapsed time for the animation
    private final Texture mermaidSheet; // The sprite sheet texture
    public final Texture projectileTexture;
    public Sound shootingSound;
    Vector2 position;
    Vector2 velocity;
    float health;
    public boolean isAlive = true;
    float shootTimer;
    float shootInterval;
    ArrayList<BubbleProjectile> projectiles;
    private final Rectangle bounds;
    private final ShapeRenderer shapeRenderer;
    private Animation<TextureRegion> bloodAnimation;
    public boolean isDying = false;
    private float explosionTimer;
    public boolean isVisible;
    public boolean hasHitbox = true;
    private final Music explosionSound;
    public int type;
    private Color tint;
    private float currentAngle = 0f;
    private int waveCounter = -2; // DO NOT CHANGE THIS VALUE. I DON'T KNOW WHY IT NEEDS TO BE -2 TO WORK BUT IT DOES
    private float timeSinceLastWave = 0f;
    private float homingSpeed = 150f;

    public EnemyMermaid (String textureFileName, int enemyType) {
        this.type = enemyType;
        this.isVisible = true;
        this.mermaidSheet = new Texture(Gdx.files.internal(textureFileName));

        // Enemy color variants
        switch (enemyType) {
            case 1:
                this.tint = new Color(0.5f, 0.7f, 1f, 1f);
                break;
            case 2:
                break;
            case 3:
                this.tint = new Color(0.75f, 0.3f, 1f, 1f);
                break;
            default:
                this.tint = Color.WHITE;
        }

        int frameWidth = (mermaidSheet.getWidth() / 6);
        int frameHeight = mermaidSheet.getHeight();
        TextureRegion[][] temp = TextureRegion.split(mermaidSheet, frameWidth, frameHeight);
        TextureRegion[] mermaidFrames = temp[0]; // Assuming all frames are in the first row

        this.mermaidAnimation = new Animation<>(0.1f, mermaidFrames);
        this.stateTime = 0f;
        this.projectiles = new ArrayList<>();
        this.position = new Vector2(Gdx.graphics.getWidth() - frameWidth, Gdx.graphics.getHeight() / 2f - frameHeight / 2f);
        this.velocity = new Vector2(-1, 0);
        this.health = 200;
        this.projectileTexture = new Texture(Gdx.files.internal("bubble.png"));
        this.shootingSound = Gdx.audio.newSound(Gdx.files.internal("bubbleshootsound.wav"));
        this.shootTimer = 0;
        this.bounds = new Rectangle(position.x, position.y, frameWidth + 7, frameHeight + 42);
        this.explosionSound = Gdx.audio.newMusic(Gdx.files.internal("splat.mp3"));

        this.shapeRenderer = new ShapeRenderer();
    }

    public void update(float deltaTime, int type, Vector2 playerPos) {
        stateTime += deltaTime;
        position.add(velocity);

        shootTimer += deltaTime;
        shootInterval = getShootInterval(type);
        if (waveCounter >= 6) {
            // Only update the timer if the current wave is complete
            timeSinceLastWave += deltaTime;
        }
        if (shootTimer >= shootInterval && isAlive) {
            shoot(type, playerPos);
            shootTimer = 0;
        }

        Iterator<BubbleProjectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            BubbleProjectile projectile = iterator.next();
            // First, update the projectile's state for this frame
            projectile.updateProjectile(deltaTime);

            // Then, check if the projectile has moved off-screen after the update
            if (isProjectileOffScreen(projectile)) {
                iterator.remove(); // Remove the projectile from the ArrayList
                projectile.dispose(); // Dispose of the projectile's resources
            }
        }
        // Update the bounds to match the new position
        bounds.setPosition(position.x + 37, position.y + 4);
        if (isDying) {
            explosionTimer += deltaTime;
            if (bloodAnimation.isAnimationFinished(explosionTimer)) {
                isDying = false;
            }
            isVisible = false; // Make the sprite disappear
        }
    }

    private float getShootInterval(int type) {
        switch (type) {
            case 1:
                return 1.0f;
            case 2:
                return 0.3f;
            case 3:
                return 0.75f;
            default:
                return 1.0f;
        }
    }

    public boolean checkCollision(Rectangle otherBounds) {
        if (hasHitbox) {
            return bounds.overlaps(otherBounds); // Check if player's bounds overlap with another object's bounds
        } else {
            return false;
        }
    }

    // Method to highlight the position of the hitbox. Used only for debugging, DO NOT DELETE YET
    public void drawHitbox() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();
    }

    private void shoot(int type, Vector2 playerPos) {
        if (type == 2) {
            if (waveCounter >= 6) {
                if (timeSinceLastWave < 2.0f) {
                    return; // Wait until the gap time has passed
                } else {
                    // Reset for a new wave
                    waveCounter = 0;
                    timeSinceLastWave = 0f;
                }
            }
        }

        BubbleProjectile newProjectile;
        Vector2 projectilePosition;
        Vector2 projectileVelocity;
        switch (type) {
            case 1:
                TextureRegion currentFrame = mermaidAnimation.getKeyFrame(stateTime, true);
                projectilePosition = new Vector2(position.x + (currentFrame.getRegionWidth() * 0.5f) - 970f,
                        position.y + (currentFrame.getRegionHeight() * 0.5f) - 920f);
                projectileVelocity = new Vector2(-250f, 0);
                newProjectile = new BubbleProjectile(projectilePosition, projectileVelocity, projectileTexture, type);
                projectiles.add(newProjectile);
                break;
            case 2:
                float radians = (float) Math.toRadians(currentAngle); // Convert the angle to radians
                float radius = 40f; // Define the radius of the radial pattern
                // Calculate the new position based on the current angle and radius
                projectilePosition = new Vector2(
                        position.x + radius * (float)Math.cos(radians) - 900f,
                        position.y + radius * (float)Math.sin(radians) - 910f
                );

                projectileVelocity = new Vector2(
                        400f * (float)Math.cos(radians),
                        400f * (float)Math.sin(radians)
                );
                newProjectile = new BubbleProjectile(projectilePosition, projectileVelocity, projectileTexture, type);
                projectiles.add(newProjectile);
                currentAngle += 360f / 6f; // Increment the angle for the next projectile

                if (currentAngle >= 360f) {
                    currentAngle = 0f; // Reset the angle after completing a full circle
                }
                System.out.println(currentAngle);
                waveCounter++;
                break;
            case 3:
                projectilePosition = new Vector2 (position.x, position.y);
                Vector2 directionToTarget = playerPos.sub(projectilePosition).nor(); // Normalized direction vector towards the target
                projectileVelocity = new Vector2(directionToTarget.x * homingSpeed, directionToTarget.y * homingSpeed); // Velocity adjusted towards the target
                newProjectile = new BubbleProjectile(projectilePosition, projectileVelocity, projectileTexture, type);
                projectiles.add(newProjectile);
                break;
        }
        if (MainMenu.isSFXEnabled()) {
            shootingSound.play(3f);
        }
    }

    public void draw(SpriteBatch batch) {
        // Draw the enemy using the current frame of the animation
        TextureRegion currentFrame = mermaidAnimation.getKeyFrame(stateTime, true);
        // Define scale factor
        float scaleFactor = 2.0f; // Adjust this value to scale up the sprite

        // Calculate scaled width and height
        float scaledWidth = currentFrame.getRegionWidth() * scaleFactor;
        float scaledHeight = currentFrame.getRegionHeight() * scaleFactor;

        float w = 5.0f * scaledWidth;
        float h = 5.0f * scaledHeight;

        if (isVisible) {
            // Set tint color before drawing
            batch.setColor(tint != null ? tint : Color.WHITE);
            batch.draw(currentFrame, position.x, position.y, scaledWidth, scaledHeight);
            // Reset color to white after drawing
            batch.setColor(Color.WHITE);
        }
        if (isDying) {
            batch.draw(bloodAnimation.getKeyFrame(explosionTimer), position.x - 160f, position.y - 220f, w, h);
        }
        for (BubbleProjectile projectile : projectiles) projectile.draw(batch);
    }

    // DO NOT CHANGE THIS X VALUE OR THE BUBBLES WILL BE DISPOSED PREMATURELY
    private boolean isProjectileOffScreen(BubbleProjectile projectile) {
        return projectile.getPosition().x < -1000;
    }

    public ArrayList<BubbleProjectile> getProjectiles() {
        return projectiles;
    }

    public void takeDamage(float amount) {
        health -= amount;
        if (health == 0) {
            isAlive = false;
            isDying = true;
            onMermaidDeath();
        }
    }

    public void onMermaidDeath() {
        Texture bloodSheet = new Texture(Gdx.files.internal("bloodSheet.png"));
        int frameCols = 6; // Number of columns in the sprite sheet
        int frameRows = 3; // Number of rows in the sprite sheet
        TextureRegion[][] tmp = TextureRegion.split(bloodSheet,
                bloodSheet.getWidth() / frameCols,
                bloodSheet.getHeight() / frameRows);
        TextureRegion[] bloodFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                bloodFrames[index++] = tmp[i][j];
            }
        }
        bloodAnimation = new Animation<TextureRegion>(0.1f, bloodFrames);
        if (MainMenu.isSFXEnabled()) {
            explosionSound.play();
        }
    }

    public void dispose() {
        mermaidSheet.dispose();
        projectileTexture.dispose();
        shootingSound.dispose();
        explosionSound.dispose();
    }
}
