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
    private float baseY;
    public boolean isVisible = true;
    private int type;
    private float angle = 0; // This will keep increasing to move in a spiral
    private float spiralRadius = 0; // Start with a small radius and increase it to expand the spiral
    private float spiralSpeed = 1.5f;
    private float spiralExpansionRate = 3f;

    public BubbleProjectile(Vector2 position, Vector2 velocity, Texture texture, int enemytype) {
        this.shapeRenderer = new ShapeRenderer();
        this.type = enemytype;
        this.amplitude = 5f;
        this.frequency = 0.01f;
        this.baseX = position.x; // Store the initial horizontal position
        this.baseY = position.y; // Store the initial vertical position
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
    public void updateProjectile(float deltaTime) {
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
                // Update the angle to keep the projectile moving
                angle += deltaTime * spiralSpeed; // spiralSpeed controls the rotation speed of the spiral
                // Update the radius to expand the spiral
                spiralRadius += deltaTime * spiralExpansionRate; // spiralExpansionRate controls how quickly the spiral expands
                // Calculate the new position using polar coordinates (r, theta) and convert them to Cartesian coordinates (x, y)
                float updatedX = baseX + spiralRadius * (float)Math.cos(angle);
                float updatedY = baseY + spiralRadius * (float)Math.sin(angle);
                // Set the projectile's position to the new calculated position
                sprite.setPosition(updatedX, updatedY);
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
