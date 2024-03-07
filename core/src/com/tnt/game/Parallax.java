package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public class Parallax {

    private class ParallaxLayer {
        public Texture texture;
        public float speed;
        public float position;

        public ParallaxLayer(Texture texture, float speed) {
            this.texture = texture;
            this.speed = speed;
            this.position = 0; // Initialize position at 0
        }
    }

    private ArrayList<ParallaxLayer> layers;
    private float elapsedTime = 0; // Keep track of elapsed time

    public Parallax() {
        layers = new ArrayList<ParallaxLayer>();
    }

    public void addLayer(Texture texture, float speed) {
        layers.add(new ParallaxLayer(texture, speed));
    }

    public void update(float delta) {
        elapsedTime += delta;
        for (ParallaxLayer layer : layers) {
            // Update layer position based on its speed and elapsed time
            layer.position += layer.speed * delta;
            // Reset position to loop the background
            if (layer.position > Gdx.graphics.getWidth()) {
                layer.position = 0;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (ParallaxLayer layer : layers) {
            // Use the updated position to draw the layer
            float xPos = -layer.position; // Adjusted drawing position based on elapsed time and layer speed
            batch.draw(layer.texture, xPos, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            // Draw the second part of the layer for looping effect
            if (xPos < Gdx.graphics.getWidth()) {
                batch.draw(layer.texture, xPos + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }
}