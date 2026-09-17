package com.fathan.frontend.objects.enemies;

import com.fathan.frontend.objects.Player;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.Collidable;
import com.badlogic.gdx.graphics.Color;


public class Fairy extends Enemy {
    public Fairy(String name, int hp) {
        super(name, hp);
        this.x = 150;
        this.y = 380;
        this.scoreValue = 500L;
    }

    public Fairy(float x, float y, String name, int hp) {
        super(name, hp);
        this.x = x;
        this.y = y;
        this.scoreValue = 500L;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other.getClass() == Player.class){
            System.out.println("Player touches fairy");
        }
    }
}
