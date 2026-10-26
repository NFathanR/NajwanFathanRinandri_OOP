package com.fathan.frontend.objects.patterns.shootingStrategy;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.patterns.ShootingPattern;

public class SpreadShot implements ShootingPattern {
    private float speed;
    private int numBullets;
    private float spreadAngleDegrees;
    private GameObject target;

    // Konstruktor Spreadshot dengan sudut statis
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees) {
        this(speed, numBullets, spreadAngleDegrees, null);
    }

    // Konstruktor Spreadshot terarah (FAN_AIMED) yang membidik target
    public SpreadShot(float speed, int numBullets, float spreadAngleDegrees, GameObject target) {
        this.speed = speed;
        this.numBullets = numBullets;
        this.spreadAngleDegrees = spreadAngleDegrees;
        this.target = target;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        // Sudut dasar: Jika ada target, bidik target; jika tidak, gunakan 90° (player) / 270° (Musuh)
        float centerAngle;
        if (target != null) {
            float targetCenterX = target.getX() + target.getWidth() / 2f;
            float targetCenterY = target.getY() + target.getHeight() / 2f;
            centerAngle = (float) Math.toDegrees(Math.atan2(targetCenterY - originY, targetCenterX - originX));
        } else {
            centerAngle = isPlayer ? 90f : 270f;
        }

        float startAngle = centerAngle - (spreadAngleDegrees / 2f);
        float angleStep = (numBullets > 1) ? (spreadAngleDegrees / (numBullets - 1)) : 0;
        int damage = isPlayer ? 25 : 15;

        for (int i = 0; i < numBullets; i++) {
            float angleDeg = startAngle + (i * angleStep);
            float angleRad = (float) Math.toRadians(angleDeg);

            // Dekomposisi kecepatan ke sumbu X dan Y
            float vx = (float) (speed * Math.cos(angleRad));
            float vy = (float) (speed * Math.sin(angleRad));

            if (isPlayer) {
                bulletManager.spawnPlayerBullet(originX, originY, vx, vy, damage);
            } else {
                bulletManager.spawnEnemyBullet(originX, originY, vx, vy, damage);
            }
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public int getNumBullets() { return numBullets; }
    public void setNumBullets(int numBullets) { this.numBullets = numBullets; }

    public float getSpreadAngleDegrees() { return spreadAngleDegrees; }
    public void setSpreadAngleDegrees(float spreadAngleDegrees) { this.spreadAngleDegrees = spreadAngleDegrees; }

    public GameObject getTarget() { return target; }
    public void setTarget(GameObject target) { this.target = target; }
}

