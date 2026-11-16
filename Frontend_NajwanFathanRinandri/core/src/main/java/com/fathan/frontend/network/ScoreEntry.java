package com.fathan.frontend.network;

public class ScoreEntry {
    private String scoreId;
    private String playerName;
    private int point;
    private String createdAt;

    public ScoreEntry() {}

    public String getScoreId() { return scoreId; }
    public String getPlayerName() { return playerName; }
    public int getPoint() { return point; }
    public String getCreatedAt() { return createdAt; }
}
