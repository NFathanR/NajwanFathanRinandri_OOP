// BossGlide.java
package com.fathan.frontend.states;

import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.patterns.entityStrategy.TargetPointMovement;

import java.util.Random;

final class BossGlide {
    private static final float MIN_X = 32f, MAX_X = 352f;
    private static final float MIN_Y = 288f, MAX_Y = 384f;
    private static final float HOME_X = 192f, HOME_Y = 336f;

    private BossGlide() {}

    static TargetPointMovement home(Boss boss, float seconds) {
        float distance = (float) Math.hypot(HOME_X - boss.getX(), HOME_Y - boss.getY());
        return new TargetPointMovement(HOME_X, HOME_Y, Math.max(30f, distance / seconds));
    }

    static TargetPointMovement random(Boss boss, float distance, float seconds, Random random) {
        double angle = random.nextDouble() * Math.PI * 2;
        float targetX = clamp(boss.getX() + (float) Math.cos(angle) * distance, MIN_X, MAX_X);
        float targetY = clamp(boss.getY() + (float) Math.sin(angle) * distance, MIN_Y, MAX_Y);
        return new TargetPointMovement(targetX, targetY, distance / seconds);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
