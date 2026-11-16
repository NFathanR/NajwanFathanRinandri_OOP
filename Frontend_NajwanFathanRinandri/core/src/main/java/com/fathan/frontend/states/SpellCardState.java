package com.fathan.frontend.states;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.systems.EntityFactory;
import com.fathan.frontend.systems.LevelWaveManager;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class SpellCardState implements WaveState {
    private static final float INTRO_SECONDS = 2f;
    private static final float TEST_TIMEOUT_SECONDS = 4f;

    private final Random random = new Random();
    private List<Supplier<Attack>> blocks;
    private Attack currentBlock;
    private int blockIndex = -1;
    private float phaseTimer = 0f;

    protected abstract String getSpellName();
    protected abstract int getStartingLife();
    protected abstract float getTimeoutSeconds();
    protected abstract List<Supplier<Attack>> createAttackBlocks(GameObject player);
    protected abstract WaveState createNextState();

    protected float getGlideDistance() { return 90f; }
    protected float getGlideSeconds() { return 1.5f; }
    protected boolean isFinalPhase() { return false; }
    protected void onSpellStart(Boss boss) {}
    protected void onSpellEnd(LevelWaveManager manager, Boss boss) {}

    // ====================================================
    // Core Methods
    // ====================================================

    // TODO: Implementasi method yang harusnya diwariskan dari WaveState, yaitu:

    // Ketika baru masuk State:
    // 1. Pakai LevelWaveManager untuk *get* `Boss`!
    // 2. Kalau Boss gaada, atau hancur, coba *spawn* di koordinat (200, 420) atas nama "Rumia", dengan HP sesuai nyawa awal spell saat ini.
    // 3. Set HP saat ini boss sesuai nyawa awal spell. (Beda dengan Non-Spell: cukup ubah HP saat ini tanpa mereset maxHp).
    // 4. Tentukan apakah boss tetap hidup saat zero HP:
    //    jika fase ini adalah *final phase*, maka boss TIDAK boleh bertahan di HP 0!
    // 5. Selama masa intro, buat boss tidak bisa kena *damage*, tetap *collidable*, dan atur *movementPattern*-nya ke posisi `home` dengan durasi `INTRO_SECONDS`.
    // 6. Jalankan hook saat spell dimulai dengan parameter `boss`.
    // 7. Isi variabel `blocks` dengan membuat blok-blok serangan memakai player dari `manager`.

    public void onEnter(LevelWaveManager manager) {
        Boss boss = manager.getBoss();
        if (boss == null || boss.isDestroyed()){
            EntityFactory.createBoss(320, 464, "Rumia", getStartingLife());
        }

        boss.setHp(getStartingLife());
        if (isFinalPhase()){
            boss.setSurvivesAtZeroHp(false);
        }
        
    }

    // Ketika meng-update:
    // 1. Tambah `phaseTimer` sebesar `delta`.
    // 2. Ambil `Boss` dari `manager` (cukup *get*, tidak perlu spawn lagi).
    // 3. Kalau Boss ada (tidak `null`), belum hancur, dan `phaseTimer >= INTRO_SECONDS`:
    //    3.1. Bila `currentBlock` masih `null`, buat agar boss kembali bisa kena *damage*.
    //    3.2. Bila `currentBlock` masih `null` ATAU `currentBlock` sudah selesai, mulai blok serangan berikutnya lewat helper yang tersedia!
    //    3.3. Update `currentBlock` dengan parameter `boss`, `bulletManager` dari `manager`, dan `delta`.
    // 4. Hitung:
    //    - timeout = (manager.getPlayer() == null) ? TEST_TIMEOUT_SECONDS : getTimeoutSeconds(),
    //    - bossDefeated = (boss != null && boss.getHp() <= 0).
    // 5. Jika `bossDefeated || phaseTimer >= timeout`:
    //    5.1. Jika bulletManager dari `manager` tidak `null`, clear enemy bullet.
    //    5.2. Jalankan hook saat spell berakhir dengan parameter `manager` dan `boss`.
    //    5.3. Buat next state, dan pindah state dengan `manager`.

    // ====================================================
    // Helper
    // ====================================================
    private void startNextBlock(Boss boss) {
        boolean firstBlock = (blockIndex < 0);
        blockIndex = (blockIndex + 1) % blocks.size();
        currentBlock = blocks.get(blockIndex).get();
        if (!firstBlock) {
            boss.setMovementPattern(BossGlide.random(boss, getGlideDistance(), getGlideSeconds(), random));
        }
        System.out.println("[" + getClass().getSimpleName() + "] Rumia block: " + currentBlock.getName());
    }
}
