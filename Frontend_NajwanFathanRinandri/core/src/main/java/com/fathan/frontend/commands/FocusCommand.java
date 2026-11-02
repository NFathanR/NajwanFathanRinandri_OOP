package com.fathan.frontend.commands;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.Player;

public class FocusCommand implements Command {
    private boolean focused;

    public FocusCommand(boolean focused) {
        // TODO: Inisialisasi atribut focused dari parameter konstruktor
        this.focused = focused;
    }

    // TODO: Override `execute` dengan logika bisnis berikut:
    // - Panggil metode setFocused(focused) milik player
    @Override
    public void execute(Player player, BulletManager bulletManager) {
        player.setFocused(focused);
    }
}
