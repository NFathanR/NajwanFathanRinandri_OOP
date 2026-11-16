package com.fathan.frontend.objects.enemies;

import com.fathan.frontend.objects.EntityShooter;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;
import com.fathan.frontend.objects.items.ItemType;
import com.fathan.frontend.objects.patterns.ShootingPattern;
import com.fathan.frontend.objects.patterns.entityStrategy.EntityMovementPattern;

import java.awt.*;

public class Enemy extends EntityShooter {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected long scoreValue;
    protected ItemType itemDrop = null;
    protected boolean damageable = true;
    protected boolean collidable = true;
    protected float shotHitboxWidth = 0f;
    protected float shotHitboxHeight = 0f;

    public Enemy(String name, int hp) {
        super(200, 380, 24,24,0,Color.PINK);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = 100L;
    }
    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue) {
        super(x, y, width,height,0,color);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }

    public Enemy(float x, float y, float width, float height, Color color, String name, int hp, long scoreValue, ShootingPattern shootingPattern, EntityMovementPattern movementPattern) {
        super(x, y, width, height, 0, color, shootingPattern, movementPattern);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.scoreValue = scoreValue;
    }


    public boolean takeDamage(int damage) {
        if (!damageable) return false;
        boolean wasAlive = isAlive();
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        if (wasAlive && this.hp == 0) {
            System.out.println(name + " was defeated!");
            this.destroy();
            return true;
        }
        return false;
    }


    public ItemType getItemDrop() { return itemDrop; }
    public void setItemDrop(ItemType itemDrop) { this.itemDrop = itemDrop; }
    public boolean isDamageable() { return damageable; }
    public void setDamageable(boolean damageable) { this.damageable = damageable; }
    public boolean isCollidable() { return collidable; }
    public void setCollidable(boolean collidable) { this.collidable = collidable; }

    public Rectangle getShotHitbox() {
        if (shotHitboxWidth <= 0f || shotHitboxHeight <= 0f) return getCoreHitbox();
        return new Rectangle(x + (width - shotHitboxWidth) / 2f,  y + (height - shotHitboxHeight) / 2f, shotHitboxWidth, shotHitboxHeight);
    }

    public void setShotHitboxSize(float width, float height) {
        this.shotHitboxWidth = width;
        this.shotHitboxHeight = height;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getName() {
        return this.name;
    }

    public int getHp() {
        return this.hp;
    }

    public long getScoreValue() {
        return this.scoreValue;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on " + player.getName() + "!");
        player.takeDamage(damage);
    }
}
