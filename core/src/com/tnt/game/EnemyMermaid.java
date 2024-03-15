package com.tnt.game;

import com.badlogic.gdx.Gdx;
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
    private final Texture projectileTexture;
    public Sound shootingSound;
    Vector2 position;
    Vector2 velocity;
    float health;
    public boolean isAlive = true;
    float shootTimer;
    float shootInterval = 2.0f; // Seconds between shots
    ArrayList<BubbleProjectile> projectiles;
    private final Rectangle bounds;
    private final ShapeRenderer shapeRenderer;

    public EnemyMermaid (String textureFileName) {
        this.mermaidSheet = new Texture(Gdx.files.internal(textureFileName));

        int frameWidth = (mermaidSheet.getWidth() / 6);
        int frameHeight = mermaidSheet.getHeight();
        TextureRegion[][] temp = TextureRegion.split(mermaidSheet, frameWidth, frameHeight);
        TextureRegion[] mermaidFrames = temp[0]; // Assuming all frames are in the first row

        this.mermaidAnimation = new Animation<>(0.1f, mermaidFrames);
        this.stateTime = 0f;
        this.projectiles = new ArrayList<>();
        this.position = new Vector2(Gdx.graphics.getWidth() - frameWidth, Gdx.graphics.getHeight() / 2f - frameHeight / 2f);
        this.velocity = new Vector2(-1, 0);
        this.health = 100;
        this.projectileTexture = new Texture(Gdx.files.internal("bubble.png"));
        this.shootingSound = Gdx.audio.newSound(Gdx.files.internal("bubbleshootsound.wav"));
        this.shootTimer = 0;
        this.bounds = new Rectangle(position.x, position.y, frameWidth + 7, frameHeight + 42);

        this.shapeRenderer = new ShapeRenderer();
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        position.add(velocity);

        shootTimer += deltaTime;
        if (shootTimer >= shootInterval) {
            shoot();
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
        // Check for collisions and other logic...
    }

    public boolean checkCollision(Rectangle otherBounds) {
        return bounds.overlaps(otherBounds); // Check if player's bounds overlap with another object's bounds
    }

    // Method to highlight the position of the hitbox. Used only for debugging, DO NOT DELETE YET
    public void drawHitbox() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();
    }

    private void shoot() {
        TextureRegion currentFrame = mermaidAnimation.getKeyFrame(stateTime, true);
        Vector2 projectilePosition = new Vector2(position.x + (currentFrame.getRegionWidth() * 0.5f) - 970f,
                position.y + (currentFrame.getRegionHeight() * 0.5f) - 920f);
        Vector2 projectileVelocity = new Vector2(-100f, 0);
        BubbleProjectile newProjectile = new BubbleProjectile(projectilePosition, projectileVelocity, projectileTexture);
        projectiles.add(newProjectile);
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
        batch.draw(currentFrame, position.x, position.y, scaledWidth, scaledHeight);
        // Draw projectiles
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
        if (health <= 0) {
            health = 0; // Prevent health from going below 0
            isAlive = false;
        }
    }

    public void dispose() {
        mermaidSheet.dispose();
        projectileTexture.dispose();
        shootingSound.dispose();
    }
}
