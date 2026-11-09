package com.fathan.frontend.commands;


import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;

import java.util.List;

public class BombCommand implements Command {
    private List<GameObject> entities;

    public BombCommand() {}

    public BombCommand(List<GameObject> entities) {
        this.entities = entities;
    }

    public void setEntities(List<GameObject> entities) {
        this.entities = entities;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.useBomb(bulletManager, entities);
        }
    }
}
