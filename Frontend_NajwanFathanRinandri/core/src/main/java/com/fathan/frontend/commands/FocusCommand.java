package com.fathan.frontend.commands;

import com.fathan.frontend.systems.BulletManager;
import com.fathan.frontend.objects.Player;

public class FocusCommand implements Command {
    private boolean focused;

    public FocusCommand(boolean focused) {
        this.focused = focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        if (player != null) {
            player.setFocused(focused);
        }
    }
}
