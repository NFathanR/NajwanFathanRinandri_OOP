package com.fathan.frontend.objects.patterns.entityStrategy;

import com.fathan.frontend.objects.GameObject;

public class DiveAndBankMovement implements EntityMovementPattern {
    private float speed;
    private float angle;
    private float diveDuration;
    private float bankAngularVelocity;
    private float bankDuration;
    private float timer = 0f;

    public DiveAndBankMovement(float speed, float initialAngle, float diveDuration,
                               float bankAngularVelocity, float bankDuration) {
        this.speed = speed;
        this.angle = initialAngle;
        this.diveDuration = diveDuration;
        this.bankAngularVelocity = bankAngularVelocity;
        this.bankDuration = bankDuration;
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public float getAngle() { return angle; }
    public void setAngle(float angle) { this.angle = angle; }

    @Override
    public void move(GameObject entity, float delta) {
        if (entity != null) {
            timer += delta;
            if (timer >= diveDuration && timer < (diveDuration + bankDuration)) {
                angle += bankAngularVelocity * delta;
            }
            float vx = (float) Math.cos(angle) * speed;
            float vy = (float) Math.sin(angle) * speed;
            entity.setX(entity.getX() + vx * delta);
            entity.setY(entity.getY() + vy * delta);
        }
    }
}
