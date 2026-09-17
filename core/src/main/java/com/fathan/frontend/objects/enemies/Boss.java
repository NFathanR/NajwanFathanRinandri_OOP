package com.fathan.frontend.objects.enemies;

import com.fathan.frontend.objects.Collidable;
import com.fathan.frontend.objects.Player;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Boss extends Enemy {
    public Boss(String name, int hp) {
        super(name, hp);
        this.x = 380;
        this.y = 400;
        this.width = 48;
        this.height = 48;
        this.color = Color.BLUE;
        this.scoreValue = 5000L;
    }
    public Boss(float x, float y, String name, int hp) {
        super(name, hp);
        this.x = x;
        this.y = y;
        this.width = 48;
        this.height = 48;
        this.color = Color.BLUE;
        this.scoreValue = 5000L;
    }

    @Override
    public void onCollision(Collidable other) {
        if (other.getClass() == Player.class){
            System.out.println("Player touches boss");
        }
    }
}
