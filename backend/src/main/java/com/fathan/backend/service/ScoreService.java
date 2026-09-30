package com.fathan.backend.service;

import com.fathan.backend.model.Score;
import com.fathan.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

// TODO: Tambahkan anota-si yang membuat Spring mengenali class ini sebagai service layer
@Service
public class ScoreService {
    // TODO: Tambahkan anotasi untuk melakukan Dependency Injection dari Instance yang sudah ada (ScoreRepository)
    // TODO: Tambahkan private field untuk ScoreRepository
    @Autowired
    private ScoreRepository scoreRepository;

    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    public Optional<Score> getScoreByID(UUID scoreID) {
        return scoreRepository.findById(scoreID);
    }

    public List<Score> getAllScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database kemudian kembalikan hasilnya
        // hint: Panggil method yang sama seperti kode yang kalian buat di TP nomor 4
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores(){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database dengan urutan pembuatan terbaru kemudian kembalikan hasilnya
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        // TODO: Gunakan scoreRepository untuk menemukan semua score yang ada di database yang memiliki point di atas nilai tertentu
        // gunakan minValue sebagai batas bawah nilai point
        return scoreRepository.findByPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        // TODO: Gunakan scoreRepository untuk mencari Top Scores dan berikan parameter yang sesuai
        return scoreRepository.findTopScores(limit);
    }

    public void deleteScore(UUID scoreId) {
        // TODO:
        // 1. Cari score yang ingin dihapus menggunakan scoreRepository kemudian simpan score tersebut (hint: lihat caranya di getScoreById())
        // 2. Cek apakah score tersebut ditemukan atau tidak dengan `.orElseThrow(()-> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));`
        // 3. Panggil delete() dari scoreRepository untuk menghapus score yang disimpan tadi

        scoreRepository.findById(scoreId).orElseThrow(()-> new RuntimeException("Score dengan ID " + scoreId + " tidak ditemukan"));
        scoreRepository.deleteById(scoreId);
    }


}
