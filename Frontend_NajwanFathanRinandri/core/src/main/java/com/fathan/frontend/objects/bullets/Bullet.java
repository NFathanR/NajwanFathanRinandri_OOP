package com.fathan.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.enemies.Enemy;
import com.fathan.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;


public class Bullet extends GameObject {
    private static final float PLAYER_SHOT_HITBOX = 12f;
    private BulletType bulletType;
    private int damage;

    private float vx;
    private float vy;
    private float angle = 90f;
    private boolean isPlayerBullet;
    private boolean grazed;
    private BulletMovementPattern movementPattern;


    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 16, 16, 400f, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
        this.vx = 0;
        this.vy = 400f;
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 16, 16, speed, Color.YELLOW);
        this.bulletType = bulletType;
        this.damage = damage;
        this.vx = 0;
        this.vy = speed;
        if (speed > 0) {
            this.vy = 270f;
        } else {
            this.vy = 90f;
        }
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }

    public void init(float x, float y, float vx, float vy, BulletType bulletType, int damage, boolean isPlayerBullet) {

        this.movementPattern = null;

        // 1. Perbarui nilai posisi (x, y) dan komponen kecepatan vektor (vx, vy) dari parameter yang diterima
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;

        // 2. Hitung sudut rotasi `angle` menggunakan rumus Math.atan2 sebagaimana dijelaskan di atas, lalu ganti ke degree.
        angle = (float) Math.toDegrees(Math.atan2(vy, vx));

        // 3. Perbarui nilai `speed` dengan menghitung panjang resultan kedua vektor kecepatan menggunakan rumus: Math.hypot sebagaimana dijelaskan di paragraf atas.
        speed = (float) Math.hypot(vx, vy);

        // 4. Simpan atribut identitas peluru (bulletType, damage, dan isPlayerBullet) sesuai nilai parameter
        this.bulletType = bulletType;
        this.damage = damage;
        this.isPlayerBullet = isPlayerBullet;

        // 5. Aktifkan kembali peluru ini (active) dan mulai ulang waktu state animasinya (stateTime) dari 0
        this.active = true;
        this.stateTime = 0;

        // 6. Pastikan status graze (grazed) direset menjadi tidak aktif agar peluru daur ulang ini dapat di-graze kembali
        this.grazed = false;
    }

    public boolean getIsPlayerBullet() {
        return isPlayerBullet;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean hasBeenGrazed() {
        return grazed;
    }

    public void setGrazed(boolean grazed) {
        this.grazed = grazed;
    }

    public BulletMovementPattern getMovementPattern() { return movementPattern; }

    public void setMovementPattern(BulletMovementPattern movementPattern) { this.movementPattern = movementPattern; }


    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy dalam format:
            //    Bullet hit [EnemyName] for [damage] DMG!
            System.out.println("Bullet hit " + enemy.getName() + " for " + damage + " DMG!" );

            // 2. Panggil takeDamage() milik Enemy dengan damage milik Bullet ini.
            enemy.takeDamage(damage);

            // 3. Bikin si bullet hancur (destroy) setelah mengenai Enemy, apapun hasilnya
            //    (baik enemy kalah atau masih hidup), krn satu bullet cuma boleh kena satu target.
            this.destroy();
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (movementPattern != null) {
            movementPattern.move(this, delta);
        } else {
            this.x += vx * delta;
            this.y += vy * delta;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (sprite != null) {
                // sprite, posisi (x, y), titik origin putar di tengah (width / 2f, height / 2f),
                // ukuran (width, height), scale (1f, 1f), dan sudut rotasi (angle - 90f)
                batch.draw(sprite, x, y, width / 2f, height / 2f, width, height, 1f, 1f, angle - 90f);
            } else {
                super.render(batch);
            }
        }
    }

    public com.badlogic.gdx.math.Rectangle getShotHitbox() {
        if (!isPlayerBullet) return getCoreHitbox();
        return new com.badlogic.gdx.math.Rectangle(
            x + (width - PLAYER_SHOT_HITBOX) / 2f,
            y + (height - PLAYER_SHOT_HITBOX) / 2f,
            PLAYER_SHOT_HITBOX, PLAYER_SHOT_HITBOX);
    }

    public float getStateTime() {
        return stateTime;
    }
}
