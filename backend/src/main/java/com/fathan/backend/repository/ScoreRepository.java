// ScoreRepository.java
package com.fathan.backend.repository;

import com.fathan.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

// TODO: Tambahkan anotasi yang menandai bahwa interface ini merupakan repository yang berinteraksi dengan database
@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {
    // TODO: buat method "findPointGreaterThan" dengan parameter [Integer minValue] yang mengembalikan List<Score>
    List<Score> findByPointGreaterThan(Integer minValue);
    // TODO: buat method "findAllByOrderByCreatedAtDesc" yang mengembalikan List<Score>
    List<Score> findAllByOrderByCreatedAtDesc();

    // TODO: buat native query seperti di TP untuk mengambil data Score s dan diurutkan berdasar s.point secara DESCENDING
    @Query("SELECT s FROM Score s ORDER BY s.point DESC")
    // TODO: buat method "findTopScores" dengan parameter Integer limit menggunakan List yang berisi Score
    List<Score> findTopScores(@Param("limit") Integer limit);
}