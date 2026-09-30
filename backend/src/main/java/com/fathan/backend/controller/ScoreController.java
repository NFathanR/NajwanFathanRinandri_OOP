package com.fathan.backend.controller;


import com.fathan.backend.model.Score;
import com.fathan.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

// TODO: tambah anotasi yang menandakan class ini merupakan REST API Controller
// TODO: tambahkan anotasi untuk mapping api dengan "/api/scores"
@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {
    // TODO: Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreService)
    // TODO: Tambahkan private field untuk ScoreService
    @Autowired
    private ScoreService scoreService;

    // GET /api/scores/{scoreId}
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {

        Optional<Score> score = scoreService.getScoreByID(scoreId);

        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score tidak ditemukan");
        }
    }

    // POST /api/scores
    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Score newScore = scoreService.createScore(score);
            return ResponseEntity.status(HttpStatus.CREATED).body(newScore);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Gagal membuat score");
        }
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/api/scores")
    public ResponseEntity<List<Score>> getAllScores() {
        // 2. Gunakan scoreService untuk memanggil getAllScores() dan simpan scores tersebut ke suatu variabel menggunakan List
        // 3. Kembalikan variabel berisi scores tersebut
        List<Score> score1 = scoreService.getAllScores();
        return ResponseEntity.ok(score1);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/api/scores/leaderboard")
    public  ResponseEntity<List<Score>> getLeaderboardByPoint(@RequestParam(defaultValue = "10") int limit){
        // 4. Gunakan scoreService untuk memanggil getLeaderboard() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 5. Kembalikan variabel berisi scores tersebut
        List<Score> score2 = scoreService.getLeaderboard(limit);
        return ResponseEntity.ok(score2);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/api/scores/above/{minValue}")
    public ResponseEntity<List<Score>> getScoresAboveValue(@PathVariable int minValue){
        // 3. Gunakan scoreService untuk memanggil getScoreAboveValue() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 4. Kembalikan variabel berisi scores tersebut
        List<Score> score3 =  scoreService.getScoreAboveValue(minValue);
        return ResponseEntity.ok(score3);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint GET beserta endpoint yang sesuai
    @GetMapping("/api/scores/recent")
    public ResponseEntity<List<Score>> getRecentScores(){
        // 2. Gunakan scoreService untuk memanggil getRecentScores() dengan parameter yang sesuai
        //    dan simpan scores tersebut ke suatu variabel menggunakan List
        // 3. Kembalikan variabel berisi scores tersebut
        List<Score> score4 =  scoreService.getRecentScores();
        return ResponseEntity.ok(score4);
    }

    // TODO:
    // 1. Beri anotasi yang sesuai untuk endpoint DELETE beserta endpoint yang sesuai
    @DeleteMapping("/api/scores/{scoreId}")
    public  ResponseEntity<?> deleteScore(@PathVariable UUID scoreId){
        // 3. buat try-catch block
        // pada try block:
        //  gunakan scoreService untuk memanggil deleteScore() dengan parameter yang sesuai
        //  kembalikan respons untuk menandakan score berhasil dihapus
        // pada catch block:
        //  kembalikan response error dengan status NOT_FOUND beserta body error yang sesuai
        try{
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok("{\"message\": \"Score deleted successfully\"}");
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
