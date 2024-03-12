package com.tnt.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class BubbleProjectile {
    private Vector2 velocity; // Speed and direction of the projectile's movement
    private Sprite sprite; // Sprite for the projectile
    private Circle bounds; // Used for collision detection

    private float amplitude; // Amplitude of the sine wave
    private float frequency; // Frequency of the sine wave
    private float baseX; // Base horizontal position

    public BubbleProjectile(Vector2 position, Vector2 velocity, Texture texture) {
        this.amplitude = 1f;
        this.frequency = 0.02f;
        this.baseX = position.x; // Store the initial horizontal position

        this.velocity = velocity;
        this.sprite = new Sprite(texture); // Create a sprite from the texture
        this.sprite.setPosition(position.x, position.y); // Set the sprite's position
        this.sprite.setScale(0.02f); // Scale the sprite

        // Initialize the bounds rectangle for collision detection, adjusted for the sprite's scale
        this.bounds = new Circle(sprite.getX() + sprite.getWidth() / 2 * sprite.getScaleX(),
                sprite.getY() + sprite.getHeight() / 2 * sprite.getScaleY(),
                sprite.getWidth() / 2 * sprite.getScaleX());
    }
    public void updateProjectile(float deltaTime) {
        // Move horizontally based on velocity
        baseX += velocity.x * deltaTime;

        // Calculate new vertical position using sine wave
        float newY = sprite.getY() + amplitude * (float)Math.sin(frequency * baseX);

        // Set the new position
        sprite.setPosition(baseX, newY);

        // Update the bounds position as well to match the new position
        bounds.setPosition(sprite.getX(), sprite.getY());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch); // Draw the sprite with its current scale
    }

    public Circle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void dispose() {

    }
}
