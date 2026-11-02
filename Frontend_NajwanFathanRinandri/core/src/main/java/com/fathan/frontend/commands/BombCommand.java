package com.fathan.frontend.commands;


import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;

import java.util.List;

public class BombCommand implements Command {
    private List<GameObject> entities;

    public BombCommand() {}

    public BombCommand(List<GameObject> entities) {
        // TODO: Inisialisasi atribut entities dari parameter konstruktor
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        // TODO: Inisialisasi entities sesuai parameter
        this.entities = entities;
    }

    // TODO: Override `execute` dengan logika bisnis berikut:
    // - Cek apabila player ada
    // - Apabila ada, panggil metode useBomb(bulletManager, entities) milik player
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player.isAlive()) {
            player.useBomb(bulletManager);
        }
    }
}
