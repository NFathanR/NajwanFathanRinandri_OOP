package com.fathan.frontend.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fathan.frontend.observers.GameObserver;

public class GameHUD implements GameObserver {
    private long score;
    private int hp;
    private int spellCards;
    private int power;
    private int grazeCount;
    private int lives = 3;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public GameHUD() {
        // Diinisialisasi secara lazy saat rendering agar aman dijalankan pada pengujian CLI tanpa layar
    }

    // TODO: Implementasikan metode dari interface GameObserver:
    // 1. onScoreChanged(long newScore) -> perbarui nilai atribut this.score
    // 2. onHpChanged(int currentHp) -> perbarui nilai atribut this.hp
    // 3. onSpellCardsChanged(int currentSpellCards) -> perbarui nilai atribut this.spellCards
    // 4. onPowerChanged(int currentPower) -> perbarui nilai atribut this.power
    // 5. onGrazeChanged(int currentGraze) -> perbarui nilai atribut this.grazeCount
    // 6. onLivesChanged(int currentLives) -> perbarui nilai atribut this.lives (Sistem Nyawa Touhou)

    @Override
    public void onScoreChanged(long newScore) {
        this.score = newScore;
    }

    @Override
    public void onHpChanged(int currentHp) {
        this.hp = currentHp;
    }

    @Override
    public void onSpellCardsChanged(int currentSpellCards) {
        this.spellCards = currentSpellCards;
    }

    @Override
    public void onPowerChanged(int currentPower) {
        this.power = currentPower;
    }

    @Override
    public void onGrazeChanged(int currentGraze) {
        this.grazeCount = currentGraze;
    }

    @Override
    public void onLivesChanged(int currentLives) {
        this.lives = currentLives;
    }


    public void renderBackground() {
        if (Gdx.graphics != null && Gdx.gl != null) {
            if (shapeRenderer == null) {
                shapeRenderer = new ShapeRenderer();
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            // Kotak batas area bermain (Playfield): X: 32..416, Y: 16..560 (Lebar 384, Tinggi 544)
            shapeRenderer.rect(32, 16, 384, 544);

            // Kotak batas panel informasi (Sidebar): X: 432..768, Y: 16..560 (Lebar 336, Tinggi 544)
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(432, 16, 336, 544);
            shapeRenderer.end();
        }
    }

    public void render(SpriteBatch batch) {
        if (batch != null) {
            if (font == null && Gdx.files != null) {
                font = new BitmapFont();
                font.setColor(Color.WHITE);
            }

            if (font != null) {
                font.draw(batch, "=== TOUHOU PRACTICUM ===", 448, 540);
                font.draw(batch, "HighScore: " + String.format("%09d", Math.max(score, 999990L)), 448, 500);
                font.draw(batch, "Score:     " + String.format("%09d", score), 448, 470);
                font.draw(batch, "Player:     " + (lives > 0 ? "★ ".repeat(lives) : "GAME OVER (Press R)"), 448, 430);
                font.draw(batch, "SpellCards: " + "★ ".repeat(Math.max(0, spellCards)), 448, 390);
                font.draw(batch, "Power:     " + power + " / 128", 448, 350);
                font.draw(batch, "Graze:     " + grazeCount, 448, 310);

                font.draw(batch, "--- CONTROLS ---", 448, 230);
                font.draw(batch, "Arrow / WASD : Move", 448, 200);
                font.draw(batch, "Shift        : Focus Mode", 448, 170);
                font.draw(batch, "Z            : Shoot", 448, 140);
                font.draw(batch, "X            : SpellCard (Bomb)", 448, 110);
                font.draw(batch, "R            : Restart (on Game Over)", 448, 80);
            }
        }
    }

    public void dispose() {
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    // TODO: Buat getter untuk score, hp, lives, spellCards, power, dan grazeCount
    int getHp() {
        return hp;
    }
    int getLives() {
        return lives;
    }
    int getSpellCards() {
        return spellCards;
    }
    int getPower() {
        return power;
    }
    int getGrazeCount() {
        return grazeCount;
    }

    void setHp(int hp) {
        this.hp = hp;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
    }

    void setPower(int power) {
        this.power = power;
    }

    void setGrazeCount(int grazeCount) {
        this.grazeCount = grazeCount;
    }
}
