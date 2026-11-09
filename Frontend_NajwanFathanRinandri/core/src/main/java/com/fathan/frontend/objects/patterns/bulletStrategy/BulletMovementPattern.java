package com.fathan.frontend.objects.patterns.bulletStrategy;

import com.fathan.frontend.objects.bullets.Bullet;

public interface BulletMovementPattern {
    void move(Bullet bullet, float delta);
}
