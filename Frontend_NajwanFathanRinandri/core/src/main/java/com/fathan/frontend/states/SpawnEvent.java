package com.fathan.frontend.states;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fathan.frontend.objects.enemies.Fairy;
import com.fathan.frontend.objects.items.ItemType;
import com.fathan.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.systems.AssetManager;
import com.fathan.frontend.systems.LevelWaveManager;

public class SpawnEvent {
    private final float triggerTime;
    private final float x;
    private final float y;
    private final String name;
    private final int hp;
    private final EntityMovementPattern movementPattern;
    private final ShootingPattern shootingPattern;
    private final ItemType itemDrop;
    private final float shootDelay;
    private final float shootInterval;
    private final int maxShots;
    private String args;

    public SpawnEvent(float triggerTime, float x, float y, String name, int hp,
                      EntityMovementPattern movementPattern, ShootingPattern shootingPattern) {
        this(triggerTime, x, y, name, hp, movementPattern, shootingPattern, null, 0.67f, 1.4f, 2);
    }

    public SpawnEvent(float triggerTime, float x, float y, String name, int hp,
                      EntityMovementPattern movementPattern, ShootingPattern shootingPattern,
                      ItemType itemDrop) {
        this(triggerTime, x, y, name, hp, movementPattern, shootingPattern, itemDrop, 0.67f, 1.4f, 2);
    }

    public SpawnEvent(float triggerTime, float x, float y, String name, int hp,
                      EntityMovementPattern movementPattern, ShootingPattern shootingPattern,
                      ItemType itemDrop, String args) {
        this(triggerTime, x, y, name, hp, movementPattern, shootingPattern, itemDrop, 0.67f, 1.4f, 2);
        this.args = args;
    }

    public SpawnEvent(float triggerTime, float x, float y, String name, int hp,
                      EntityMovementPattern movementPattern, ShootingPattern shootingPattern,
                      ItemType itemDrop, float shootDelay, float shootInterval, int maxShots) {
        this.triggerTime = triggerTime;
        this.x = x;
        this.y = y;
        this.name = name;
        this.hp = hp;
        this.movementPattern = movementPattern;
        this.shootingPattern = shootingPattern;
        this.itemDrop = itemDrop;
        this.shootDelay = shootDelay;
        this.shootInterval = shootInterval;
        this.maxShots = maxShots;
    }

    public float getTriggerTime() { return triggerTime; }

    public Fairy execute(LevelWaveManager manager) {
        Fairy fairy = manager.spawnFairy(x, y, name, hp);
        if (args != null && args.equalsIgnoreCase("red")) {
            Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
            fairy.setAnimation(idleAnim);
        }
        if (movementPattern != null) {
            fairy.setMovementPattern(movementPattern);
        }
        fairy.setShootingPattern(shootingPattern);
        fairy.setShootTiming(shootDelay, shootInterval, maxShots);
        if (itemDrop != null) {
            fairy.setItemDrop(itemDrop);
        }
        return fairy;
    }
}
