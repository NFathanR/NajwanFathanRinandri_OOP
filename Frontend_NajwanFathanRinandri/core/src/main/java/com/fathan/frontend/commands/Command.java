package com.fathan.frontend.commands;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.Player;

public interface Command {
    void execute(Player player, BulletManager bulletManager);
}

