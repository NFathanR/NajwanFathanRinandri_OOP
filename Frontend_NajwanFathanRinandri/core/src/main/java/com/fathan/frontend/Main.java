package com.fathan.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fathan.frontend.commands.Command;
import com.fathan.frontend.commands.InputHandler;
import com.fathan.frontend.objects.patterns.entityStrategy.TargetPointMovement;
import com.fathan.frontend.objects.patterns.shootingStrategy.SpreadShot;
import com.fathan.frontend.systems.*;
import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.Player;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.enemies.Fairy;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.items.ItemType;


import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Input;
import com.fathan.frontend.ui.GameHUD;

import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameHUD gameHUD;
    private StageFacade stage;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameHUD = new GameHUD();
        AssetManager.getInstance().init();
        stage = new StageFacade(gameHUD);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input != null && Gdx.input.isKeyJustPressed(Input.Keys.R)
            && (stage.isGameOver() || stage.isStageClear())) {
            stage.restart();
            gameHUD.clearScoreboard();
        }
        stage.update(delta);
        gameHUD.setStageClear(stage.isStageClear());
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        batch.begin();
        stage.render(batch);
        gameHUD.render(batch);
        batch.end();
        gameHUD.renderBackground();
        stage.renderOverlay(gameHUD.getShapeRenderer());
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (gameHUD != null) gameHUD.dispose();
        AssetManager.getInstance().dispose();
    }
}

