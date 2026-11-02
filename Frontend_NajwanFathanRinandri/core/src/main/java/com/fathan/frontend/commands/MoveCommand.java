package com.fathan.frontend.commands;

import com.fathan.frontend.objects.BulletManager;
import com.fathan.frontend.objects.Player;

public class MoveCommand implements Command {
    private float dx;
    private float dy;

    public MoveCommand(float dx, float dy) {
        // TODO: Inisialisasi atribut dx dan dy dari parameter konstruktor
        this.dx = dx;
        this.dy = dy;
    }

    public void set(float dx, float dy) {
        // TODO: set dx & dy sesuai parameter
        this.dx = dx;
        this.dy = dy;
    }

    // TODO: Override `execute` dengan logika bisnis berikut:
    // - Panggil metode move(dx, dy) milik player
    // - Update animasi player

    @Override
    public void execute(Player player, BulletManager bulletManager) {
        player.move(dx, dy);
        player.updateAnimationState(dx);
    }

}
