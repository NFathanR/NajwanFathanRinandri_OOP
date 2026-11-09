package com.fathan.frontend.objects.patterns.entityStrategy;

import com.fathan.frontend.objects.GameObject;

public interface EntityMovementPattern {
    public void move(GameObject entity, float delta);
}

