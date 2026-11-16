package com.fathan.frontend.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fathan.frontend.network.ScoreEntry;
import com.fathan.frontend.observers.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class GameHUD implements GameObserver {
    private long score;
    private int hp;
    private int spellCards;
    private int power;
    private int grazeCount;
    private int lives = 3;

    private static final float PLAYFIELD_CENTER_X = 32f + 384f / 2f;
    private boolean stageClear = false;
    private String scoreboardStatus = null;
    private final List<String> leaderboardLines = new ArrayList<>();
    private GlyphLayout glyphLayout;

    private BitmapFont font;
    private ShapeRenderer shapeRenderer;

    public GameHUD() { }

    @Override public void onScoreChanged(long newScore) { this.score = newScore; }
    @Override public void onHpChanged(int currentHp) { this.hp = currentHp; }
    @Override public void onSpellCardsChanged(int currentSpellCards) { this.spellCards = currentSpellCards; }
    @Override public void onPowerChanged(int currentPower) { this.power = currentPower; }
    @Override public void onGrazeChanged(int currentGraze) { this.grazeCount = currentGraze; }
    @Override public void onLivesChanged(int currentLives) { this.lives = currentLives; }

    public void renderBackground() {
        if (Gdx.graphics != null && Gdx.gl != null) {
            if (shapeRenderer == null) shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            shapeRenderer.rect(32, 16, 384, 448);
            float sidebarWidth = Gdx.graphics.getWidth() == 640 ? 192f : 336f;
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(432, 16, sidebarWidth, 448);
            shapeRenderer.end();
        }
    }

    public void render(SpriteBatch batch) {
        if (batch == null) return;
        if (font == null && Gdx.files != null) {
            font = new BitmapFont();
            font.setColor(Color.WHITE);
        }
        if (font == null) return;
        if (glyphLayout == null) glyphLayout = new GlyphLayout();

        font.draw(batch, "=== TOUHOU PRACTICUM ===", 448, 445);
        font.draw(batch, "HighScore: " + String.format("%09d", Math.max(score, 999990L)), 448, 415);
        font.draw(batch, "Score:     " + String.format("%09d", score), 448, 385);
        font.draw(batch, "Player:     " + (lives > 0 ? "★ ".repeat(lives) : "GAME OVER"), 448, 355);
        font.draw(batch, "SpellCards: " + "★ ".repeat(Math.max(0, spellCards)), 448, 325);
        font.draw(batch, "Power:     " + power + " / 128", 448, 295);
        font.draw(batch, "Graze:     " + grazeCount, 448, 265);

        font.draw(batch, "--- CONTROLS ---", 448, 215);
        font.draw(batch, "Arrow / WASD : Move", 448, 185);
        font.draw(batch, "Shift        : Focus Mode", 448, 155);
        font.draw(batch, "Z / U        : Shoot", 448, 125);
        font.draw(batch, "X / I        : SpellCard (Bomb)", 448, 95);
        font.draw(batch, "R            : Restart (Clear/Over)", 448, 65);

        if (stageClear || lives <= 0) renderRunEndLeaderboard(batch);
    }

    private void renderRunEndLeaderboard(SpriteBatch batch) {
        font.setColor(stageClear ? Color.GOLD : Color.SALMON);
        drawCenteredOnPlayfield(batch, stageClear ? "=== STAGE 1 CLEAR! ===" : "=== GAME OVER ===", 360f);
        font.setColor(Color.WHITE);
        drawCenteredOnPlayfield(batch, "--- LEADERBOARD ---", 320f);
        if (scoreboardStatus != null) drawCenteredOnPlayfield(batch, scoreboardStatus, 295f);
        for (int i = 0; i < leaderboardLines.size(); i++) {
            drawCenteredOnPlayfield(batch, leaderboardLines.get(i), 265f - i * 24f);
        }
        drawCenteredOnPlayfield(batch, "Press R to Restart", 120f);
    }

    private void drawCenteredOnPlayfield(SpriteBatch batch, String text, float y) {
        if (text == null) return;
        glyphLayout.setText(font, text);
        font.draw(batch, glyphLayout, PLAYFIELD_CENTER_X - glyphLayout.width / 2f, y);
    }

    public void setStageClear(boolean stageClear) { this.stageClear = stageClear; }
    public void setScoreboardStatus(String status) { this.scoreboardStatus = status; }

    public void showLeaderboard(List<ScoreEntry> entries) {
        leaderboardLines.clear();
        if (entries == null) return;
        for (int i = 0; i < entries.size(); i++) {
            ScoreEntry entry = entries.get(i);
            leaderboardLines.add((i + 1) + ". " + entry.getPlayerName() + "  " + String.format("%09d", entry.getPoint()));
        }
    }

    public void clearScoreboard() {
        stageClear = false;
        scoreboardStatus = null;
        leaderboardLines.clear();
    }

    public void dispose() {
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    public ShapeRenderer getShapeRenderer() { return shapeRenderer; }
    public long getScore() { return score; }
    public int getHp() { return hp; }
    public int getLives() { return lives; }
    public int getSpellCards() { return spellCards; }
    public int getPower() { return power; }
    public int getGrazeCount() { return grazeCount; }
}
