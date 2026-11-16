package com.fathan.frontend.objects.patterns.entityStrategy;

import com.fathan.frontend.objects.GameObject;

public class StopAndGoMovement implements EntityMovementPattern {
    private float initialSpeed;
    private float currentSpeed;
    private float angle;
    private float moveDuration;
    private float pauseDuration;
    private float turnAngularVelocity;
    private float turnDuration;
    private float exitAcceleration;
    private float timer = 0f;
    private boolean hasShot = false;

    public StopAndGoMovement(float initialSpeed, float initialAngle, float moveDuration,
                             float pauseDuration, float turnAngularVelocity, float turnDuration,
                             float exitAcceleration) {
        this.initialSpeed = initialSpeed;
        this.currentSpeed = initialSpeed;
        this.angle = initialAngle;
        this.moveDuration = moveDuration;
        this.pauseDuration = pauseDuration;
        this.turnAngularVelocity = turnAngularVelocity;
        this.turnDuration = turnDuration;
        this.exitAcceleration = exitAcceleration;
    }

    public StopAndGoMovement(float initialSpeed, float initialAngle, float moveDuration,
                             float pauseDuration, boolean exitRight, float exitAcceleration) {
        this(initialSpeed, initialAngle, moveDuration, pauseDuration,
            exitRight ? (float) Math.PI : -(float) Math.PI, 1.0f, exitAcceleration);
    }

    public boolean isHovering() {
        return timer >= moveDuration && timer < (moveDuration + pauseDuration);
    }

    public boolean shouldFireSpread() {
        if (!hasShot && timer >= (moveDuration + 0.17f)) {
            hasShot = true;
            return true;
        }
        return false;
    }

    public float getTimer() { return timer; }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity != null) {
            timer += delta;
            if (timer < moveDuration) {
                currentSpeed = initialSpeed;
            } else if (timer < moveDuration + pauseDuration) {
                currentSpeed = 0f;
            } else {
                float escapeTime = timer - (moveDuration + pauseDuration);
                if (escapeTime < turnDuration) {
                    angle += turnAngularVelocity * delta;
                }
                currentSpeed += exitAcceleration * delta;
            }

            float vx = (float) Math.cos(angle) * currentSpeed;
            float vy = (float) Math.sin(angle) * currentSpeed;
            entity.setX(entity.getX() + vx * delta);
            entity.setY(entity.getY() + vy * delta);
        }
    }
}
