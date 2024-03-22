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
    public float currentHp;
    public GameScore gameScore;
    public int currentScore;
    private boolean toggleEnemyType = false;

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
        Gdx.input.setInputProcessor(stage); // Set input processorr
        Skin skin = new Skin(Gdx.files.internal("pixthulhu-ui.json"));

        // Initialize HealthStatus (Heart Images)
        this.healthStatus = new HealthStatus(game, stage);

        this.gameStatus = new GameStatus(game, skin);
        gameStatus.addToStage(stage); // Add the setting button to the stage

        this.gameScore = new GameScore(stage, skin);
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
                    currentHp = player.takeDamage(100f);
                    notifyHealthChanged(currentHp);
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
                    if (enemy.health >= 0) {
                        playerProjectile.dispose(); // Dispose of the player projectile resources
                        playerProjectileIterator.remove(); // Remove the projectile from the player's list
                    }
                    if (enemy.health == 0) { // Must be exactly 0 to prevent score from incrementing again after death
                        gameScore.addScore(500);
                        currentScore = gameScore.getScore();
                        player.notifyScoreChanged(currentScore);
                    }
                }
            }
        }
    }

    public void notifyHealthChanged(float currentHp) {
        // Convert health points to lives if necessary
        int currentLives = calculateLivesFromHealth(currentHp);
        healthStatus.updateHearts(currentLives);
    }
    
    private int calculateLivesFromHealth(float currentHp) {
        // Implement logic to convert health points to number of lives
        // This is just a placeholder implementation
        return (int)Math.ceil(currentHp / 100f);
    }

    private void removeDeadEnemies() {
        Iterator<EnemyMermaid> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            EnemyMermaid enemy = iterator.next();
            if (!enemy.isAlive && !enemy.isDying) { // Check if the enemy is not alive
                enemy.isVisible = false;
                enemy.hasHitbox = false;
            }
        }
    }

    public void updateSpawn(float deltaTime, GameScore gameScore) {
        currentScore = gameScore.getScore(); // problematic line
        // Update spawn timer
        spawnTimer += deltaTime;
        // Check if it's time to spawn a new enemy
        if (spawnTimer >= spawnInterval) {
            spawnEnemy(currentScore);
            spawnTimer = 0; // Reset the spawn timer
        }

        Iterator<EnemyMermaid> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            EnemyMermaid enemy = iterator.next();
            // First, update the enemy's state for this frame
            enemy.update(deltaTime, enemy.type);

            // Then, check if the enemy has moved off-screen after the update. DO NOT CHANGE THIS X VALUE
            if (enemy.position.x < -1000) {
                iterator.remove(); // Remove the enemy from the ArrayList
                enemy.dispose(); // Dispose of the enemy's resources
            }
        }
        checkCollisions();
        removeDeadEnemies();
    }

    private void spawnEnemy(int currentScore) {
        int enemyType;
        EnemyMermaid newEnemy = null;
        if (currentScore < 2000) {
            newEnemy = new EnemyMermaid("mermaid.png", 1);
        } else if (currentScore >= 2000 && currentScore < 5000) {
            spawnInterval = 3f;
            // Spawn 2 different enemy types alternately
            enemyType = toggleEnemyType ? 2 : 1;
            newEnemy = new EnemyMermaid("mermaid.png", enemyType);
            toggleEnemyType = !toggleEnemyType;
        } else if (currentScore >= 5000 && currentScore <= 10000) {
            // difficulty increase
        } // add more difficulty settings here
        if (newEnemy != null) { // Verify that newEnemy is not null before spawning
            // Use the height of the first frame of the animation for initial positioning
            float enemyHeight = newEnemy.mermaidAnimation.getKeyFrames()[0].getRegionHeight();
            // Calculate a random y position within the screen height bounds
            float randomY = (float) Math.random() * (Gdx.graphics.getHeight() - (enemyHeight * 2));

            // Set the enemy's initial position to spawn on the right edge of the screen at a random y-coordinate
            newEnemy.position.set(Gdx.graphics.getWidth(), randomY);

            // Add the new enemy to your collection of enemies
            enemies.add(newEnemy);
        }
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
        if (MainMenu.isSFXEnabled()) {
            levelbgm.play(); // Resume background music
        } else {
            levelbgm.pause(); // Pause background music
        }
        gameStatus.setLevelBGM(levelbgm);

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
        updateSpawn(delta, gameScore);
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
        if (player.playerIsDead) { // Upon player death, stop all vfx and sfx
            if (MainMenu.isSFXEnabled()) {
                levelbgm.stop();
                player.shootingSound.stop();
                for (EnemyMermaid enemy : enemies) {
                    enemy.shootingSound.stop();
                    enemy.isAlive = false;
                    Iterator<BubbleProjectile> iterator = enemy.getProjectiles().iterator();
                    while (iterator.hasNext()) {
                        BubbleProjectile projectile = iterator.next();
                        projectile.isVisible = false; // Set to invisible. Disposing directly will make it show up as a black box
                    }
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