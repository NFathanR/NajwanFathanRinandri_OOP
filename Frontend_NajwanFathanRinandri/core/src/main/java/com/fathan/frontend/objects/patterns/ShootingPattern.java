package com.fathan.frontend.objects.patterns;

import com.fathan.frontend.objects.BulletManager;

public interface ShootingPattern {
    void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer);
}

