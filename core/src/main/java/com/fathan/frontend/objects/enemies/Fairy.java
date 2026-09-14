package com.fathan.frontend;

import java.awt.*;

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
}
