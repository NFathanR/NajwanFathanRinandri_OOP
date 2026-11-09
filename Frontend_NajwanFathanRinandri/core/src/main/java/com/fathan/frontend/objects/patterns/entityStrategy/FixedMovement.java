package com.fathan.frontend.objects.patterns.entityStrategy;

import com.fathan.frontend.objects.GameObject;

public class FixedMovement implements EntityMovementPattern {
    @Override
    public void move(GameObject entity, float delta) {
        // Fixed position: tidak ada perpindahan posisi yang diterapkan pada entity
    }
}
