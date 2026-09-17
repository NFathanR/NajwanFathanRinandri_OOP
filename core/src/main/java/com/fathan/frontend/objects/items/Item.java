package com.fathan.frontend.objects.items;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Item extends GameObject {
    private String itemType;
    private long scoreValue;
    private ItemType itemTypeEnum;

    public String getItemType() {
        return itemType;
    }

    public long getScoreValue() {
        return scoreValue;
    }

    public Item(float x, float y, String itemType) {
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = 1000L;
    }

    public Item(float x, float y, float width, float height, float speed, String itemType, long scoreValue) {
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemType;
        this.scoreValue = scoreValue;
    }

    public Item(float x, float y, ItemType itemTypeEnum){
        super(x, y, 16, 16, 100f, Color.WHITE);
        this.itemType = itemTypeEnum.name();
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public Item(float x, float y, float width, float height, float speed, ItemType itemTypeEnum, long scoreValue){
        super(x, y, width, height, speed, Color.WHITE);
        this.itemType = itemTypeEnum.name();
        this.scoreValue = itemTypeEnum.getScoreValue();
    }

    public ItemType getItemTypeEnum() {
        return itemTypeEnum;
    }

    @Override
    public void update(float delta) {
        this.y -= speed * delta;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other.getClass() == Player.class){
        }
    }


}
