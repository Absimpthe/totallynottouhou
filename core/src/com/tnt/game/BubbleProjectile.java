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

    public BubbleProjectile(Vector2 position, Vector2 velocity, Texture texture) {
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
        // Adjust the sprite's position based on the velocity and the time passed since the last frame
        sprite.setPosition(sprite.getX() + velocity.x * deltaTime, sprite.getY() + velocity.y * deltaTime);
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
