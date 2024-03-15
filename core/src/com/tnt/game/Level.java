package com.tnt.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Iterator;

public class Level implements Screen {
    private final aquamarine game;
    private SpriteBatch batch;
    private SpriteBatch bgbatch;
    private SpriteBatch fadebatch;
    private Music levelbgm;
    private Parallax ParallaxBG;
    private Player player;
    private ArrayList<EnemyMermaid> enemies;
    private Texture blackTexture;
    private float alpha = 1f;
    private float spawnTimer;
    private float spawnInterval = 5f;
    private HealthStatus healthStatus;
    private Stage stage;
    private GameStatus gameStatus;

    public Level(aquamarine game) {
        this.game = game;
        // Load the sprite
        batch = new SpriteBatch();
        // Initialize the player
        player = new Player("submarine.png", game);
        // Initialize enemies
        enemies = new ArrayList<EnemyMermaid>();
        spawnTimer = 0;

        // Initialize stage and skin
        this.stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));

        // Initialize HealthStatus
        this.healthStatus = new HealthStatus(game, skin);
        healthStatus.addToStage(stage); // Add the health bar to the stage

        this.gameStatus = new GameStatus(game, skin);
        gameStatus.addToStage(stage); // Add the setting button to the stage
    }

    private void checkCollisions() {
        // Iterate over each enemy
        for (EnemyMermaid enemy : enemies) {
            // Now iterate over each projectile managed by the current enemy
            Iterator<BubbleProjectile> iterator = enemy.getProjectiles().iterator();
            while (iterator.hasNext()) {
                BubbleProjectile projectile = iterator.next();
                if (player.checkCollision(projectile.getBounds())) {
                    // Handle collision
                    player.takeDamage(100f);
                    projectile.dispose(); // Dispose of the projectile resources
                    iterator.remove(); // Remove the projectile from the enemy's list using the iterator
                }
            }
        }
        // Iterate over each player projectile
        Iterator<PlayerProjectile> playerProjectileIterator = player.getProjectiles().iterator();
        while (playerProjectileIterator.hasNext()) {
            PlayerProjectile playerProjectile = playerProjectileIterator.next();
            // Now iterate over each enemy to check collision with the current projectile
            for (EnemyMermaid enemy : enemies) {
                if (enemy.checkCollision(playerProjectile.getBounds())) {
                    enemy.takeDamage(50f);
                    playerProjectile.dispose(); // Dispose of the player projectile resources
                    playerProjectileIterator.remove(); // Remove the projectile from the player's list
                }
            }
        }
    }

    private void removeDeadEnemies() {
        Iterator<EnemyMermaid> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            EnemyMermaid enemy = iterator.next();
            if (!enemy.isAlive) { // Check if the enemy is not alive
                iterator.remove(); // Remove the enemy from the list
            }
        }
    }

    public void updateSpawn(float deltaTime) {
        // Update spawn timer
        spawnTimer += deltaTime;

        // Check if it's time to spawn a new enemy
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0; // Reset the spawn timer
        }

        Iterator<EnemyMermaid> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            EnemyMermaid enemy = iterator.next();
            // First, update the enemy's state for this frame
            enemy.update(deltaTime);

            // Then, check if the enemy has moved off-screen after the update. DO NOT CHANGE THIS X VALUE
            if (enemy.position.x < -1000) {
                iterator.remove(); // Remove the enemy from the ArrayList
                enemy.dispose(); // Dispose of the enemy's resources
            }
        }
        checkCollisions();
        removeDeadEnemies();
    }

    private void spawnEnemy() {
        // Spawn enemy at desired position
        EnemyMermaid newEnemy = new EnemyMermaid("mermaid.png");
        // Use the height of the first frame of the animation for initial positioning
        float enemyHeight = newEnemy.mermaidAnimation.getKeyFrames()[0].getRegionHeight();
        // Calculate a random y position within the screen height bounds
        float randomY = (float) Math.random() * (Gdx.graphics.getHeight() - (enemyHeight * 2));

        // Set the enemy's initial position to spawn on the right edge of the screen at a random y-coordinate
        newEnemy.position.set(Gdx.graphics.getWidth(), randomY);

        // Add the new enemy to your collection of enemies
        enemies.add(newEnemy);
    }

    public void draw(SpriteBatch batch) {
        // Draw all enemies
        for (EnemyMermaid enemy : enemies) {
            enemy.draw(batch);
        }
    }

    @Override
    public void show() {
        /*---------------
        Load and play background music
        --------------- */
        levelbgm = Gdx.audio.newMusic(Gdx.files.internal("levelBGM.mp3"));
        levelbgm.setLooping(true);
        levelbgm.setVolume(0.5f);
        if (MainMenu.isMusicOn) {
            levelbgm.play(); // Resume background music
        } else {
            levelbgm.pause(); // Pause background music
        }

        /*---------------
        Initialize parallax background
        --------------- */
        bgbatch = new SpriteBatch();
        ParallaxBG = new Parallax();
        ParallaxBG.addLayer(new Texture("far.png"), 30f); // Farthest layer
        ParallaxBG.addLayer(new Texture("sand.png"), 60f); // Middle layer
        ParallaxBG.addLayer(new Texture("foreground-merged.png"), 90f);   // Closest layer

        // Initialize pixmap for fade-in effect
        fadebatch = new SpriteBatch();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // 1x1 pixel, you can scale it later
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render parallax background
        ParallaxBG.update(delta);
        bgbatch.begin();
        ParallaxBG.render(bgbatch);
        bgbatch.end();
        // Draw the sprites
        batch.begin();
        player.draw(batch);
        draw(batch);
        batch.end();
        player.update(delta);
        updateSpawn(delta);
        // Fade-in effect
        float fadeSpeed = 1f;
        alpha -= fadeSpeed * delta;
        alpha = Math.max(alpha, 0); // Ensure alpha doesn't go below 0
        // Enable blending and draw the black texture with the current alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        fadebatch.begin();
        fadebatch.setColor(1, 1, 1, alpha); // Set the batch color to include the alpha for transparency
        fadebatch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw full screen
        fadebatch.setColor(Color.WHITE); // Reset batch color to avoid affecting other textures
        fadebatch.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Update and draw the health bar
        healthStatus.update(delta);
        stage.act(delta);
        stage.draw();
        // Used to draw hitboxes for debugging. Comment out if not needed, DO NOT DELETE
        player.drawHitbox();
        for (EnemyMermaid enemy : enemies) {
            enemy.drawHitbox();
            // Now iterate over each projectile managed by the current enemy
            Iterator<BubbleProjectile> iterator = enemy.getProjectiles().iterator();
            while (iterator.hasNext()) {
                BubbleProjectile projectile = iterator.next();
                projectile.drawHitbox();
            }
        }
        if (player.playerIsDead) {
            if (MainMenu.isMusicOn) {
                levelbgm.stop();
            }
            if (MainMenu.isSFXEnabled()) {
                player.shootingSound.stop();
                for (EnemyMermaid enemy : enemies) {
                    enemy.shootingSound.stop();
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void hide() {

    }
    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (player != null) player.dispose();
        if (fadebatch != null) fadebatch.dispose();
        if (stage != null) stage.dispose();
    }
}