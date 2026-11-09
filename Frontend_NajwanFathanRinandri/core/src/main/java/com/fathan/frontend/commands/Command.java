package com.fathan.frontend.commands;

import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.objects.Player;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}

