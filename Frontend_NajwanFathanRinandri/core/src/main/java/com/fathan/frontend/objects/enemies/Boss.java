package com.fathan.frontend.objects.enemies;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.objects.patterns.shootingStrategy.SpreadShot;

import java.awt.*;

public class Boss extends Enemy {
    // TODO: Deklarasikan field private `shootingPattern` bertipe ShootingPattern
    //       dan langsung inisialisasi (new) dengan `SpreadShot` yang baru saja kita buat.
    //       Pastikan speed-nya bernilai 200f, angle spread-nya 30f, dan ada 3 peluru (num) yang ditembak.

    private ShootingPattern shootingPattern = new SpreadShot(200f, 3, 30f);

    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
    }

    // TODO: Buat method shootBullet(BulletManager bulletManager):
    //       Jika shootingPattern tidak null dan bos belum hancur,
    //       maka eksekusi shootingPattern pada koordinat *cone* tengah bos (x + width / 2 - 8, y)
    public void shootBullet(BulletManager bulletManager) {
        if (shootingPattern != null && !isDestroyed()) {
            shootingPattern.execute(x + width / 2 - 8, y, bulletManager, false);
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            System.out.println("Player touches boss");
        }
    }

    // TODO: Sediakan getter dan setter untuk shootingPattern
    public ShootingPattern getShootingPattern() {return shootingPattern; }
    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }
}
