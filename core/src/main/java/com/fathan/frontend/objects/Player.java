package com.fathan.frontend.objects;

import com.fathan.frontend.objects.enemies.Enemy;
import com.fathan.frontend.objects.items.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.fathan.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class    Player extends GameObject {
    private String name;
    private int hp;
    private int power;
    private int spellCards;
    private long score;

    public String getName() {
        return name;
    }

    public int getHp(){
        return hp;
    }

    public int getPower() {
        return power;
    }

    public int getSpellCards() {
        return spellCards;
    }

    public long getScore(){
        return score;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public void setPower(int power){
        this.power = power;
    }

    public void setSpellCards(int spellCards){
        this.spellCards = spellCards;
    }

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32,32, 100, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(int x, int y, String name, int hp, int power, int spellCards) {
        super(x, y, 32,32, 100, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public void takeDamage(int damage) {
        setHp(getHp() - damage);

        if (hp > 0) {
            System.out.println(this.name + " took " + damage + " damage! Remaining HP: " + this.hp);
        } else {
            System.out.println(this.name + " has been defeated!");
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + getPower();
        System.out.println(getName() + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        if(target.takeDamage(damage)){
            addScore(target.getScoreValue());
        }
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            System.out.println(getName() + " gained " + points + " pts! Total Score: " + this.score);
        }
    }

    public void collectItem(Item item) {
        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    addScore(item.getScoreValue());
                    System.out.println(this.name + "collected POWER item! Power increased to " + this.power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(this.name + "collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards += 1;
                    addScore(item.getScoreValue());
                    System.out.println(this.name + "collected BOMB item! SpellCards: " + this.spellCards);
                }
                case LIFE -> {
                    this.hp += 20;
                    addScore(item.getScoreValue());
                    System.out.println(this.name + "collected LIFE item! HP: " + this.hp);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }
    }

    @Override
    public void update(float delta) {
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                y += speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                y -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                x -= speed * delta;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                x += speed * delta;
            }

        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other.getClass() == Item.class){
            System.out.println("Player touches items");
            collectItem((Item) other);
        }
    }
}
