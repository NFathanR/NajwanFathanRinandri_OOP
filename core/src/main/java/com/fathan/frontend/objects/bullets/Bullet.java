package com.fathan.frontend.objects.bullets;

import com.fathan.frontend.objects.BulletType;
import com.badlogic.gdx.graphics.Color;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.enemies.Enemy;

public class Bullet extends GameObject {
    private BulletType bulletType;
    private int damage;

    public Bullet(float x, float y, BulletType bulletType, int damage) {
        super(x, y, 8, 16, 400f, Color.YELLOW);
        // TODO: inisialisasi bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
    }

    public Bullet(float x, float y, float speed, BulletType bulletType, int damage) {
        super(x, y, 8, 16, speed, Color.YELLOW);
        // TODO: inisialisasi bulletType dan damage dari parameter
        this.bulletType = bulletType;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        // TODO: posisi y bertambah sebesar speed * delta (bullet bergerak ke atas)
        this.y = y + (speed * delta);
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Enemy enemy) {
            // 1. Tampilkan pesan bahwa Bullet mengenai Enemy dalam format:
            //    Bullet hit [EnemyName] for [damage] DMG!
            System.out.println("Bullet hit" + enemy.getName() + "for" + damage + "DMG!");

            // 2. Panggil takeDamage() milik Enemy dengan damage milik Bullet ini.
            enemy.takeDamage(damage);

            // 3. Bikin si bullet hancur (destroy) setelah mengenai Enemy, apapun hasilnya
            //    (baik enemy kalah atau masih hidup), krn satu bullet cuma boleh kena satu target.
            this.destroy();

        }
    }

    public BulletType getBulletType() { return bulletType; }
    public void setBulletType(BulletType bulletType) { this.bulletType = bulletType; }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
}
