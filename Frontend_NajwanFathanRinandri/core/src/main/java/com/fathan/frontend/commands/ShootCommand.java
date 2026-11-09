package com.fathan.frontend.commands;

import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.objects.Player;

public class ShootCommand implements Command {
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.shootBullet(bulletManager);
        }
    }
}
