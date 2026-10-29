package com.fathan.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.CollisionReferee;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.enemies.Fairy;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.items.ItemType;


import java.util.*;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Input;
import com.fathan.frontend.systems.AssetManager;
import com.fathan.frontend.systems.EntityFactory;

import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    private Player player;
    private Fairy fairy;
    private Boss boss;
    private Item powerItem;
    private Item pointItem;
    private List<GameObject> entities;
    private SpriteBatch batch;
    // TODO: Buat field private untuk menyimpan BulletManager
    private BulletManager bulletManager;
    // TODO: Buat field private untuk menyimpan CollisionReferee
    private CollisionReferee collisionReferee;
    private float enemyShootTimer = 0f;

    @Override
    public void create() {
        // TODO: instanceiasi BulletManager baru
        bulletManager = new BulletManager();
        // TODO: instanceiasi CollisionReferee baru
        collisionReferee = new CollisionReferee();

        // TODO 1:
        // Pada inisialisasi renderer, buat SpriteBatch dan simpan ke batch.
        // Hint LibGDX: new SpriteBatch()
        batch = new SpriteBatch();


        // TODO 2:
        // Inisialisasi list fairy dan entities sebagai ArrayList kosong.
        List<Fairy> fairy = new ArrayList<>();
        entities = new ArrayList<>();

        // TODO 3:
        // Sebelum membuat entitas, ambil instance AssetManager dan panggil init().
        AssetManager.getInstance().init();

        // TODO 4:
        // Ubah pembuatan semua entitas! Ikuti tabel dan buat Player, Fairy, Boss, dan Item
        // agar memakai metode EntityFactory yang benar.
        // Untuk kedua Fairy, masukkan mereka ke list fairy menggunakan add(...).
        player = EntityFactory.createPlayer(280, 40, "Reimu Hakurei", 100, 15, 3);
        fairy.add(EntityFactory.createFairy(150, 380, "Red Fairy", 20));
        fairy.add(EntityFactory.createFairy(250, 380, "Blue Fairy", 20, "fairy_idle_blue"));
        boss = EntityFactory.createBoss(380, 400, "Rumia", 150);
        powerItem = EntityFactory.createItem(200, 450, ItemType.POWER);
        pointItem = EntityFactory.createItem(320, 480, ItemType.POINT);

        // TODO 5:
        // Masukkan semua objek yang baru saja kita buat ke dalam entities.
        entities.add(player);
        entities.add(boss);
        entities.add(pointItem);
        entities.add(powerItem);
        entities.addAll(fairy);
    }

    private void updateEnemyScheduler(float delta) {
        enemyShootTimer += delta;
        if (enemyShootTimer >= 1.5f) {
            boss.shootBullet(bulletManager);
            enemyShootTimer = 0f;
        }
    }

    @Override
    public void render() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        float delta = Gdx.graphics.getDeltaTime();

        // TODO: Buat input player. Jika tombol Z ditekan panggil
        // player.shootBullet(bulletManager);
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z) || Gdx.input.isKeyJustPressed(Input.Keys.X)){
            player.shootBullet(bulletManager);
        }

        // TODO: Scheduler tembakan berkala Bos Rumia
        updateEnemyScheduler(delta);

        // TODO: Generic update & clean untuk entitas standar (Player, Enemy, Item)
        updateAndClean(entities, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // TODO: Update dan daur ulang Object Pool
        bulletManager.update(delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        collisionReferee.resolveCollisions(player, entities, bulletManager);
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        for (GameObject obj : entities) {
            obj.update(delta);
        }

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                GameObject a = entities.get(i);
                GameObject b = entities.get(j);

                // TODO: Cek apakah getCoreHitbox() milik a dan b saling overlap (gunakan method .overlaps() milik Rectangle)
                // TODO: Panggil a.onCollision(b) dan b.onCollision(a)
                if(a.getCoreHitbox().overlaps(b.getCoreHitbox())) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (GameObject entity : entities) {
            if(!entity.isDestroyed()) {
                entity.render(shapeRenderer);
            }
        }
        shapeRenderer.end();
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.begin();
        for (GameObject entity : entities) {
            if (!entity.isDestroyed()) {
                // TODO: Buat agar setiap entity melakukan method .render() dengan mengoper parameter SpriteBatch.
                entity.render(batch);
            }
        }
        bulletManager.render(batch);
        batch.end();

    }

    // Non-static (Instance) Generic Method with Bounded Type Parameter <T extends GameObject>
    public <T extends GameObject> void updateAndClean(List<T> list, float delta, float screenWidth, float screenHeight) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            entity.update(delta);

            if (entity.isOffScreen(screenWidth, screenHeight) || entity.isDestroyed()) {
                System.out.println("Removed via Generic Iterator: " + entity.getClass().getSimpleName());
                iterator.remove();
            }
        }
    }


    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }

        // TODO: Panggil dispose untuk AssetManager agar Texture yang dimuat juga dilepas.
        AssetManager.getInstance().dispose();
    }
}
