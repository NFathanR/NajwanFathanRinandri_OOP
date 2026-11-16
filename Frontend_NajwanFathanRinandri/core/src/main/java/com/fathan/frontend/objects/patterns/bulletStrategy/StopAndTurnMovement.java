package com.fathan.frontend.objects.patterns.bulletStrategy;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.bullets.Bullet;

public class StopAndTurnMovement implements BulletMovementPattern {
    private final float stopTime;
    private final float newSpeed;
    private final float turnDegrees;
    private final GameObject aimTarget;

    public StopAndTurnMovement(float stopTime, float newSpeed, float turnDegrees, GameObject aimTarget) {
        this.stopTime = stopTime;
        this.newSpeed = newSpeed;
        this.turnDegrees = turnDegrees;
        this.aimTarget = aimTarget;
    }

    public static StopAndTurnMovement turnBy(float stopTime, float newSpeed, float turnDegrees) {
        return new StopAndTurnMovement(stopTime, newSpeed, turnDegrees, null);
    }

    public static StopAndTurnMovement reaimAt(float stopTime, float newSpeed, GameObject target) {
        return new StopAndTurnMovement(stopTime, newSpeed, 0f, target);
    }

    @Override
    public void move(Bullet bullet, float delta) {
        if (bullet == null) return;

        float t = bullet.getStateTime();
        if (t < stopTime) {
            float launchSpeed = (float) Math.hypot(bullet.getVx(), bullet.getVy());
            bullet.setSpeed(launchSpeed * (1f - t / stopTime));
        } else if (t - delta < stopTime) {
            float baseAngle = bullet.getAngle();
            if (aimTarget != null) {
                float targetCenterX = aimTarget.getX() + aimTarget.getWidth() / 2f;
                float targetCenterY = aimTarget.getY() + aimTarget.getHeight() / 2f;
                baseAngle = (float) Math.toDegrees(Math.atan2(targetCenterY - bullet.getY(), targetCenterX - bullet.getX()));
            }
            bullet.setAngle(baseAngle + turnDegrees);
            bullet.setSpeed(newSpeed);
        }

        float rad = (float) Math.toRadians(bullet.getAngle());
        bullet.setX(bullet.getX() + (float) Math.cos(rad) * bullet.getSpeed() * delta);
        bullet.setY(bullet.getY() + (float) Math.sin(rad) * bullet.getSpeed() * delta);
    }

    public float getStopTime() { return stopTime; }
    public float getNewSpeed() { return newSpeed; }
    public float getTurnDegrees() { return turnDegrees; }
    public GameObject getAimTarget() { return aimTarget; }
}
