package com.fathan.frontend.states;

import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.systems.LevelWaveManager;

public class StageClearState implements WaveState {
    @Override
    public void onEnter(LevelWaveManager manager) {
        System.out.println("[Stage] STAGE CLEAR!");
        Boss boss = manager.getBoss();
        if (boss != null && !boss.isDestroyed()) boss.destroy();
    }
    @Override
    public void update(LevelWaveManager manager, float delta) { }
}

