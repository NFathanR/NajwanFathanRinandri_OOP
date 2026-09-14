package com.fathan.frontend;

import java.awt.*;

public class Player extends GameObject {
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
        super(280, 40, 32,32, 0, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(int x, int y, String name, int hp, int power, int spellCards) {
        super(x, y, 32,32, 0, Color.RED);
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
        System.out.println(getName() + " collected " + item.getItemType() + "!");
        if (item.getScoreValue() > 0) {
            addScore(item.getScoreValue());
        }
    }
}
