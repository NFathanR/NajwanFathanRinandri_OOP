// Attack.java
package com.fathan.frontend.states;

import com.fathan.frontend.objects.EntityShooter;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.systems.BulletManager;

import java.util.ArrayList;
import java.util.List;

public class Attack {
    private static class Volley {
        private final float time;
        private final ShootingPattern pattern;

        private Volley(float time, ShootingPattern pattern) {
            this.time = time;
            this.pattern = pattern;
        }
    }

    private final String name;
    private final List<Volley> volleys = new ArrayList<>();
    private float duration = 0f;
    private float elapsed = 0f;
    private int nextVolley = 0;

    public Attack(String name) {
        this.name = name;
    }

    public Attack at(float timeSeconds, ShootingPattern pattern) {
        volleys.add(new Volley(timeSeconds, pattern));
        duration = Math.max(duration, timeSeconds);
        return this;
    }

    public Attack thenRest(float seconds) {
        float lastVolleyTime = volleys.isEmpty() ? 0f : volleys.get(volleys.size() - 1).time;
        duration = lastVolleyTime + seconds;
        return this;
    }

    public void update(EntityShooter shooter, BulletManager bulletManager, float delta) {
        elapsed += delta;
        while (nextVolley < volleys.size() && volleys.get(nextVolley).time <= elapsed) {
            if (shooter != null && bulletManager != null) {
                shooter.setShootingPattern(volleys.get(nextVolley).pattern);
                shooter.shootBullet(bulletManager);
            }
            nextVolley++;
        }
    }

    public boolean isFinished() {
        return nextVolley >= volleys.size() && elapsed >= duration;
    }

    public String getName() { return name; }
    public float getDuration() { return duration; }
    public int getVolleyCount() { return volleys.size(); }
}
