package com.nama.frontend;

public class Enemy {
    String name;
    int hp;
    int maxHp;

    public Enemy(String name, int hp) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! HP: " + this.hp + "/" + this.maxHp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated!");
        }
    }

    public void attack(Player player, int damage) {
        System.out.println(name + " unleashes bullet barrage on " + player.name + "!");
        player.takeDamage(damage);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}
