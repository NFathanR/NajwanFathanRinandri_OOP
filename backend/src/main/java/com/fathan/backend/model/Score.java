package com.fathan.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "score_id")
    private UUID scoreId;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name= "points", nullable = false)
    private Integer point;

    @CreationTimestamp
    @Column(name = "date_achieved", updatable = false)
    private LocalDateTime createdAt;

    public Score(){}

    public Score(String playerName, Integer point){
        this.playerName = playerName;
        this.point = point;
    }

    //getter setter
    public UUID getScoreId() {
        return scoreId;
    }

    public void setScoreId(UUID playerId) {
        this.scoreId = playerId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer score) {
        this.point = score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
