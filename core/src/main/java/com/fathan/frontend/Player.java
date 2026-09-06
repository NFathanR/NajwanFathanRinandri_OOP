package com.nama.frontend;

ublic class Player {
    String name;
    int hp;
    int power;
    int spellCards;

    public Player(String name, int hp, int power, int spellCards) {
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.name + " dealing " + damage + " DMG!");
        target.takeDamage(damage);
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
        }
    }

    public boolean isAlive() {
        return this.hp > 0;
    }
}
