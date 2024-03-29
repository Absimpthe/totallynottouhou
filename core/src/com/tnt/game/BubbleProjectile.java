package com.tnt.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BubbleProjectile {
    private ShapeRenderer shapeRenderer;
    private final Vector2 velocity; // Speed and direction of the projectile's movement
    private Sprite sprite; // Sprite for the projectile
    private Rectangle bounds; // Used for collision detection
    private final float amplitude; // Amplitude of the sine wave
    private final float frequency; // Frequency of the sine wave
    private float baseX; // Base horizontal position
    public boolean isVisible = true;
    private int type;
    private float homingSpeed = 300f;
    private float homingTimeElapsed = 0f;

    public BubbleProjectile(Vector2 position, Vector2 velocity, Texture texture, int enemytype) {
        this.shapeRenderer = new ShapeRenderer();
        this.type = enemytype;
        this.amplitude = 6f;
        this.frequency = 0.01f;
        this.baseX = position.x; // Store the initial horizontal position
        this.velocity = velocity;
        this.sprite = new Sprite(texture); // Create a sprite from the texture
        this.sprite.setPosition(position.x, position.y); // Set the sprite's position
        this.sprite.setScale(0.02f); // Scale the sprite

        // Initialize the bounds rectangle for collision detection, adjusted for the sprite's scale
        this.bounds = new Rectangle(
                sprite.getX() + sprite.getWidth() / 2 * sprite.getScaleX(), // X position
                sprite.getY() + sprite.getHeight() / 2 * sprite.getScaleY(), // Y position
                sprite.getWidth() * sprite.getScaleX(), // Width
                sprite.getHeight() * sprite.getScaleY() // Height
        );
    }
    public void updateProjectile(float deltaTime, Vector2 playerPos) {
        // Move horizontally based on velocity
        baseX += velocity.x * deltaTime;
        switch (type) {
            case 1:
                // Calculate new vertical position using sine wave
                float newY = sprite.getY() + amplitude * (float) Math.sin(frequency * baseX);
                // Set the new position
                sprite.setPosition(baseX, newY);
                break;
            case 2:
                // Update the new position based on the projectile's velocity
                float newX2 = sprite.getX() + velocity.x * deltaTime;
                float newY2 = sprite.getY() + velocity.y * deltaTime;
                // Set the new position
                sprite.setPosition(newX2, newY2);
                break;
            case 3:
                if (homingTimeElapsed < 2.0f) {
                    Vector2 targetPos = new Vector2(playerPos.x - 400f, playerPos.y - 700f);
                    Vector2 currentPosition = new Vector2(sprite.getX(), sprite.getY());
                    Vector2 directionToTarget = targetPos.sub(currentPosition).nor(); // Calculate the normalized direction vector towards the target

                    // Update velocity to move towards the target
                    velocity.x = directionToTarget.x * homingSpeed; // homingSpeed controls how fast the projectile moves
                    velocity.y = directionToTarget.y * homingSpeed;
                    homingTimeElapsed += deltaTime;
                }
                // Use the updated velocity to calculate the new position
                float newX3 = sprite.getX() + velocity.x * deltaTime;
                float newY3 = sprite.getY() + velocity.y * deltaTime;
                sprite.setPosition(newX3, newY3);
                break;
            default:
                break;
        }
        // Update the bounds to match the new position. DO NOT CHANGE THESE VALUES OR THE HITBOX WILL SPAWN IN THE WRONG POSITION
        bounds.setPosition(sprite.getX() + 943f, sprite.getY() + 941f);
    }

    public void drawHitbox() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Begin ShapeRenderer
        shapeRenderer.setColor(Color.RED); // Set color for visibility
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height); // Draw the rectangle
        shapeRenderer.end(); // End ShapeRenderer
    }

    public void draw(SpriteBatch batch) {
        if (isVisible) {
            sprite.draw(batch); // Draw the sprite with its current scale
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void dispose() {
    }
}
