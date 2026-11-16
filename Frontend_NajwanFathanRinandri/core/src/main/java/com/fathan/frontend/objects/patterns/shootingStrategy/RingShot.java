// RingShot.java
package com.fathan.frontend.objects.patterns.shootingStrategy;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.bullets.Bullet;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.fathan.frontend.systems.BulletManager;

public class RingShot implements ShootingPattern {
    private float bulletSpeed;
    private int bulletCount;
    private int damage;
    private BulletMovementPattern bulletMovementPattern;
    private GameObject target;          // null = fixed ring, otherwise bullet #0 is aimed at the target
    private float angleOffset = 0f;     // degrees
    private int layers = 1;
    private float minSpeed;
    private String spriteKey;           // null = BulletManager default sprite

    public RingShot(float bulletSpeed, int bulletCount, int damage, BulletMovementPattern bulletMovementPattern) {
        this.bulletSpeed = bulletSpeed;
        this.bulletCount = Math.max(3, bulletCount);
        this.damage = damage;
        this.bulletMovementPattern = bulletMovementPattern;
        this.minSpeed = bulletSpeed;
    }

    public RingShot(float bulletSpeed, int bulletCount, int damage) {
        this(bulletSpeed, bulletCount, damage, null);
    }

    // Fluent configuration (ECL CIRCLE parameters). Each returns this so a volley can be described in one expression.
    public RingShot aimedAt(GameObject target) {
        this.target = target;
        return this;
    }

    public RingShot withAngleOffset(float angleOffsetDegrees) {
        this.angleOffset = angleOffsetDegrees;
        return this;
    }

    public RingShot withLayers(int layers, float minSpeed) {
        this.layers = Math.max(1, layers);
        this.minSpeed = minSpeed;
        return this;
    }

    public RingShot withSpriteKey(String spriteKey) {
        this.spriteKey = spriteKey;
        return this;
    }

    @Override
    public void execute(float originX, float originY, BulletManager bulletManager, boolean isPlayer) {
        if (bulletManager == null) return;

        float baseAngle = angleOffset;
        if (target != null) {
            float targetCenterX = target.getX() + target.getWidth() / 2f;
            float targetCenterY = target.getY() + target.getHeight() / 2f;
            baseAngle += (float) Math.toDegrees(Math.atan2(targetCenterY - originY, targetCenterX - originX));
        }

        float angleStep = 360f / bulletCount;
        for (int layer = 0; layer < layers; layer++) {
            float layerSpeed = (layers > 1) ? bulletSpeed - (bulletSpeed - minSpeed) * layer / (layers - 1) : bulletSpeed;

            for (int i = 0; i < bulletCount; i++) {
                float angle = baseAngle + i * angleStep;
                float rad = (float) Math.toRadians(angle);
                float vx = (float) (Math.cos(rad) * layerSpeed);
                float vy = (float) (Math.sin(rad) * layerSpeed);

                Bullet bullet;
                if (isPlayer) {
                    bullet = bulletManager.spawnPlayerBullet(originX, originY, vx, vy, damage);
                } else if (spriteKey != null) {
                    bullet = bulletManager.spawnEnemyBullet(originX, originY, vx, vy, damage, spriteKey);
                } else {
                    bullet = bulletManager.spawnEnemyBullet(originX, originY, vx, vy, damage);
                }

                if (bullet != null) {
                    // Explicit polar state so movement strategies work even for zero-speed spawns
                    bullet.setAngle(angle);
                    bullet.setSpeed(layerSpeed);
                    if (bulletMovementPattern != null) {
                        bullet.setMovementPattern(bulletMovementPattern);
                    }
                }
            }
        }
    }

    public float getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(float bulletSpeed) { this.bulletSpeed = bulletSpeed; }

    public int getBulletCount() { return bulletCount; }
    public void setBulletCount(int bulletCount) { this.bulletCount = Math.max(3, bulletCount); }

    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }

    public BulletMovementPattern getBulletMovementPattern() { return bulletMovementPattern; }
    public void setBulletMovementPattern(BulletMovementPattern bulletMovementPattern) { this.bulletMovementPattern = bulletMovementPattern; }

    public GameObject getTarget() { return target; }
    public float getAngleOffset() { return angleOffset; }
    public int getLayers() { return layers; }
    public float getMinSpeed() { return minSpeed; }
    public String getSpriteKey() { return spriteKey; }
}
