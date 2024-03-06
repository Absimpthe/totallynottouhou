package com.tnt.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PlayerProjectile {
    private Vector2 position; // Position of the projectile on the screen
    private Vector2 velocity; // Speed and direction of the projectile's movement
    private Texture texture; // Texture of the projectile
    private Rectangle bounds; // Used for collision detection

    // Constructor to initialize the projectile with its position, velocity, and texture
    public PlayerProjectile(Vector2 position, Vector2 velocity, Texture texture) {
        this.position = position;
        this.velocity = velocity;
        this.texture = texture; // Assign the texture passed from outside
        // Initialize the bounds rectangle for collision detection, assuming the texture is already loaded
        this.bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    // Update method to be called every frame, updates the projectile's position
    public void updateProjectile(float deltaTime) {
        // Adjust the position based on the velocity and the time passed since the last frame
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        // Update the bounds position as well to match the new position
        bounds.setPosition(position.x, position.y);
    }

    // Draw method to render the projectile on the screen
    public void draw(SpriteBatch batch) {
        // Draw the texture at the projectile's current position
        batch.draw(texture, position.x, position.y);
    }

    // Getter method for the bounds, used for collision detection
    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    // Dispose method not needed if texture is managed outside this class
}
