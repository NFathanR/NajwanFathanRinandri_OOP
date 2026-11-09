package com.fathan.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fathan.frontend.commands.Command;
import com.fathan.frontend.commands.InputHandler;
import com.fathan.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.fathan.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.systems.CollisionReferee;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.enemies.Fairy;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.items.ItemType;


import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Input;
import com.fathan.frontend.systems.AssetManager;
import com.fathan.frontend.systems.EntityFactory;
import com.fathan.frontend.ui.GameHUD;

import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private List<Fairy> fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;

    private BulletManager bulletManager;
    private CollisionReferee collisionReferee;
    private InputHandler inputHandler;
    private GameHUD gameHUD;
    private float enemyShootTimer = 0f;
    private float waveTimer = 0f;
    private int wave1SpawnIndex = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        fairy = new ArrayList<>();
        entities = new ArrayList<>();
        bulletManager = new BulletManager();
        collisionReferee = new CollisionReferee();
        inputHandler = new InputHandler();
        gameHUD = new GameHUD();

        // 1. Centralized Asset Registration (AssetManager Singleton + Flyweight)
        AssetManager.getInstance().init();

        // 2. Instantiate entities via Factory Pattern (EntityFactory)
        player = EntityFactory.createPlayer(200, 50, "Reimu Hakurei", 100, 15, 3);
        boss = EntityFactory.createBoss(200, 420, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        // 3. Register GameHUD as Observer to Player (Observer Pattern)
        player.registerObserver(gameHUD);

        entities.add(player);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);

        // 4. Configure InputHandler with entities list for Bomb drops
        inputHandler.setEntities(entities);

        bulletManager.setEntities(entities);
        // TODO (3 poin): Suntikkan strategi awal ke Bos Rumia
        boss.setMovementPattern(new TargetPointMovement(200f, 420f, 150f));
        boss.setShootingPattern(new SpreadShot(220f, 5, 25f, 10, player, "bullet_danmaku_red"));

    }
    
    private void updateFairyWave(float delta) {
        float[] spawnX = {60f, 68f, 76f, 84f, 92f};
        waveTimer += delta;
        if (wave1SpawnIndex < spawnX.length && waveTimer >= 1.0f + wave1SpawnIndex * 0.267f) {
            float x = spawnX[wave1SpawnIndex++];
            Fairy f = EntityFactory.createFairy(x, 480f, "Left S-Curve Fairy", 8, "fairy_idle_blue");

            // TODO (2 poin): Tetapkan Movement Pattern ke f sebagai SCurveMovement dengan speed 130f, initialAngle (float) Math.toRadians(270), dan mirrored false
            // TODO (1 poin): Tetapkan Shooting Pattern ke f sebagai null

            fairy.add(f);
            entities.add(f);
        }
    }

    private void resetFairyWave() {
        waveTimer = 0f;
        wave1SpawnIndex = 0;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 0. Restart Game on 'R' key when Game Over
        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.R) && player.isGameOver()) {
            restartGame();
        }

        // 1. Process Input via Command Pattern (Bob Nystrom Invoker Command Stream)
        List<Command> commands = inputHandler.handleInput(delta);
        for (Command command : commands) {
            command.execute(player, bulletManager);
        }

        // 2. Periodic Enemy Action Scheduler (Foreshadows Module 9 strategy updates)
        updateEnemyScheduler(delta);

        // 3. Generic update & safe removal of standard entities
        updateAndClean(entities, delta, 416, 560);

        // 4. Object Pool Update (BulletManager)
        bulletManager.update(delta, 416, 560);

        // 5. Mediator Pattern Collision Resolution (CollisionReferee - Core vs Graze detection)
        collisionReferee.resolveCollisions(player, entities, bulletManager);

        // 6. Clear screen
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // 7. Render active entity sprites, pooled bullets, and Observer UI HUD with SpriteBatch
        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                entity.render(batch);
            }
        }
        bulletManager.render(batch);
        gameHUD.render(batch);
        batch.end();

        // 8. Render UI Frame Lines & Focus Mode Core Hurtbox Indicator (Drawn ON TOP of sprites!)
        gameHUD.renderBackground();
        player.renderFocusIndicator(gameHUD.getShapeRenderer());
    }

    // Periodic Enemy Action Scheduler (Foreshadows Module 9 dynamic strategy updates)
    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
    }

    private void restartGame() {
        System.out.println("[Main] Restarting game...");
        entities.clear();
        bulletManager.clearEnemyBullets();
        bulletManager.clearPlayerBullets();
        entities.add(player);
        player.resetGame();

        fairy.clear();
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(250, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(180, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(220, 480, ItemType.POINT);

        entities.addAll(fairy);
        entities.add(boss);
        entities.add(powerItem);
        entities.add(pointItem);
    }

    // Generic Instance Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove(); // Safe removal using Iterator!
            }
        }
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (gameHUD != null) {
            gameHUD.dispose();
        }
        AssetManager.getInstance().dispose();
    }
}
