package com.fathan.frontend.objects.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.enemies.Enemy;


public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    // TODO: Deklarasikan atribut-atribut private berikut:
    // 1. float vx dan vy (komponen kecepatan vektor 2D)
    private float vx;
    private float vy;
    // 2. float angle dengan nilai awal 90f (sudut rotasi visual peluru dalam derajat)
    private float angle = 90f;
    // 3. boolean isPlayerBullet (penanda apakah peluru ini milik player atau musuh)
    private boolean isPlayerBullet;
    // 4. boolean grazed (penanda apakah peluru ini sudah pernah di-graze oleh player)
    private boolean grazed;

    // Konstruktor dengan kecepatan default (speed = 400f)
    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 16, 16, 400f, Color.YELLOW);
        // TODO:
        // 1. Inisialisasi atribut bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
        // 2. Beri kecepatan awal vx sebagai nol dan vy sebagai 400f (bergerak lurus ke atas)
        this.vx = 0;
        this.vy = 400f;
        // 3. Set sudut rotasi visual angle agar 90 derajat ke atas
        this.angle = 90f;
        // 4. Tandai peluru ini sebagai dirinya adalah milik player (isPlayerBullet), lalu tandai status graze dalam keadaan tidak aktif
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    // Konstruktor overload dengan kecepatan custom
    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 16, 16, speed, Color.YELLOW);
        // TODO:
        // 1. Inisialisasi atribut bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
        // 2. Beri kecepatan awal vx sebagai nol dan vy sesuai dengan speed
        this.vx = 0;
        this.vy = speed;
        // 3. Set sudut rotasi visual angle: jika speed bernilai negatif gunakan 270f (menghadap bawah), jika tidak gunakan 90f
        if (speed < 0) {
            this.angle = 270f;
        } else {
            this.angle = 90f;
        }
        // 4. Tandai peluru ini sebagai dirinya adalah milik player (isPlayerBullet), lalu tandai status graze dalam keadaan tidak aktif
        this.isPlayerBullet = true;
        this.grazed = false;
    }

    // Pool Re-initialization untuk Object Pool Pattern
    public void init(float x, float y, float vx, float vy, BulletType bulletType, int damage, boolean isPlayerBullet) {
        // TODO:
        // 1. Perbarui nilai posisi (x, y) dan komponen kecepatan vektor (vx, vy) dari parameter yang diterima
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        // 2. Hitung sudut rotasi `angle` menggunakan rumus Math.atan2 sebagaimana dijelaskan di atas, lalu ganti ke degree.
        angle = (float) Math.atan2(vy,vx);
        // 3. Perbarui nilai `speed` dengan menghitung panjang resultan kedua vektor kecepatan menggunakan rumus: Math.hypot sebagaimana dijelaskan di paragraf atas.
        speed = (float) Math.hypot(vx,vy);
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

    @Override
    public void update(float delta) {
        super.update(delta);
        // TODO:
        // 1. Perbarui posisi x bertambah sebesar komponen kecepatan vx dikali delta
        this.x = vx * delta;
        // 2. Perbarui posisi y bertambah sebesar komponen kecepatan vy dikali delta
        this.y = vy * delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active) {
            if (sprite != null) {
                // TODO: Gambar sprite peluru menggunakan batch.draw() dengan parameter:
                batch.draw(sprite, x, y, width / 2f, width / 2f, width, height, 1f, 1f, angle = 90f);
                // sprite, posisi (x, y), titik origin putar di tengah (width / 2f, height / 2f),
                // ukuran (width, height), scale (1f, 1f), dan sudut rotasi (angle - 90f)
            } else {
                super.render(batch);
            }
        }
    }


    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            System.out.println("Bullet hit " + enemy.getName() + " for " + damage + " DMG!");
            boolean defeated = enemy.takeDamage(damage);
            this.destroy(); // Bullet is destroyed on collision
        }
    }

    public BulletType getBulletType() { return bulletType; }
    public int getDamage() { return damage; }

    // TODO:
    // 1. Buat getter untuk isPlayerBullet, vx, dan vy
    // 2. Buat getter dan setter untuk angle
    // 3. Buat getter hasBeenGrazed() yang mengembalikan nilai atribut grazed
    // 4. Buat setter setGrazed(boolean grazed)
    public boolean isPlayerBullet() { return isPlayerBullet; }
    public float getVx() { return vx; }
    public float getVy() { return vy; }

    public float getAngle() { return angle; }
    public void setAngle(float angle) { this.angle = angle; }

    public boolean hasBeenGrazed() { return grazed; }
    public void setGrazed(boolean grazed) { this.grazed = grazed; }
}


