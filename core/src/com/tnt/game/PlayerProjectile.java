package com.tnt.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class PlayerProjectile {
    private Vector2 velocity; // Speed and direction of the projectile's movement
    private Sprite sprite; // Sprite for the projectile
    private Rectangle bounds; // Used for collision detection

    public PlayerProjectile(Vector2 position, Vector2 velocity, Texture texture) {
        this.velocity = velocity;
        this.sprite = new Sprite(texture); // Create a sprite from the texture
        this.sprite.setPosition(position.x, position.y); // Set the sprite's position
        this.sprite.setScale(2.0f); // Scale the sprite (e.g., 2x size)

        // Initialize the bounds rectangle for collision detection, adjusted for the sprite's scale
        this.bounds = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth() * sprite.getScaleX(), sprite.getHeight() * sprite.getScaleY());
    }

    public void updateProjectile(float deltaTime) {
        // Adjust the sprite's position based on the velocity and the time passed since the last frame
        sprite.setPosition(sprite.getX() + velocity.x * deltaTime, sprite.getY() + velocity.y * deltaTime);
        // Update the bounds position as well to match the new position
        bounds.setPosition(sprite.getX(), sprite.getY());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch); // Draw the sprite with its current scale
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
