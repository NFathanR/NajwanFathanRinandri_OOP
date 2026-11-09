package com.fathan.frontend.objects.enemies;

import com.fathan.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.objects.patterns.shootingStrategy.SpreadShot;

public class Boss extends Enemy {
    public Boss(String name, int hp) {
        super(380, 400, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Suntikkan strategi bawaan Boss via setter
        // 1. Tetapkan Shooting Pattern sebagai Spread Shot dengan speed 200f, 3 peluru, dan 30f spreadAngleDegerees).
        // 2. Tetapkan Movement Pattern dengan Fixed Movement.
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
    }

    public Boss(float x, float y, String name, int hp) {
        super(x, y, 64, 64, Color.BLUE, name, hp, 5000L);
        // TODO (2 poin): Lakukan penyuntikan strategi yang sama pada konstruktor koordinat kustom
        setShootingPattern(new SpreadShot(200f, 3, 30f));
        setMovementPattern(new FixedMovement());
    }
}
