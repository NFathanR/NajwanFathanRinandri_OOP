package com.fathan.frontend.commands;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.Player;

public class ShootCommand implements Command {
    // TODO: Override `execute` dengan logika bisnis berikut:
    // - Cek apabila player ada
    // - Apabila ya, panggil metode shootBullet(bulletManager) milik player
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player.isAlive()) {
            player.shootBullet(bulletManager);
        }
    }
}
