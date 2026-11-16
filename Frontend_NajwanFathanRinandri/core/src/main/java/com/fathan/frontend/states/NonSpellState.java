package com.fathan.frontend.states;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.patterns.entityStrategy.EntityMovementPattern;
import com.fathan.frontend.objects.patterns.entityStrategy.FixedMovement;
import com.fathan.frontend.systems.EntityFactory;
import com.fathan.frontend.systems.LevelWaveManager;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class NonSpellState implements WaveState {
    private static final float TEST_TIMEOUT_SECONDS = 4f;
    private static final float GLIDE_DISTANCE = 90f;
    private static final float GLIDE_SECONDS = 1f;

    private final Random random = new Random();
    private List<Supplier<Attack>> attacks;
    private Attack currentAttack;
    private int currentIndex;
    private float phaseTimer = 0f;

    protected abstract String getPhaseName();
    protected abstract int getPhaseLife();
    protected abstract int getLifeThreshold();
    protected abstract List<Supplier<Attack>> createAttacks(GameObject player);
    protected abstract float getTimeoutSeconds();
    protected abstract WaveState createNextState();

    protected float getIntroSeconds() { return 0f; }
    protected boolean isCollidableDuringIntro() { return true; }
    protected EntityMovementPattern createIntroMovement(Boss boss) { return new FixedMovement(); }

    // ====================================================
    // Core Methods
    // ====================================================

    // TODO: Implementasi method yang harusnya diwariskan dari WaveState, yaitu:

    // Ketika baru masuk State:
    // 1. Pakai LevelWaveManager untuk *get* `Boss`!
    // 2. Kalau Boss gaada, atau hancur, coba *spawn* di koordinat (320, 464) atas nama "Rumia", dengan HP sesuai fase saat ini.
    // 3. Tetapkan nyawa bos agar sesuai HP fase saat ini.
    // 4. Tetapkan agar boss hidup saat zero HP.
    // 5. Cek *introSeconds* sekarang apakah lebih dari 0:
    //    - Apabila YA: Boss tidak bisa kena *damage*, *collidable*-nya sesuai dengan *ketentuan collidable di intro*, dan buatlah intro movement untuk *movementPattern*-nya.
    //    - Apabila TIDAK: Boss bisa kena *damage*, bisa *collidable*, dan *movementPattern*-nya adalah `BossGlide.random(boss, GLIDE_DISTANCE, GLIDE_SECONDS, random)`.
    // 6. Isi variabel `attacks` dengan cara membuat attack.
    //    Player untuk parameter pembuatan attack diambil dari `manager`.
    // 7. Tetapkan agar `currentAttack` adalah `null`.

    public void onEnter(LevelWaveManager manager) {
        Boss boss = manager.getBoss();
        if (boss == null || boss.isDestroyed()){
            EntityFactory.createBoss(320, 464, "Rumia", getPhaseLife());
        }

        boss.setHp(getPhaseLife());
        boss.setSurvivesAtZeroHp(boss.isSurvivesAtZeroHp());

        if(getIntroSeconds() > 0){
            boss.setDamageable(false);
            boss.setCollidable(isCollidableDuringIntro());
            boss.setMovementPattern(createIntroMovement(boss));
        }else{
            boss.setDamageable(true);
            boss.setCollidable(true);
            boss.setMovementPattern(BossGlide.random(boss, GLIDE_DISTANCE, GLIDE_SECONDS, random));
        }

        attacks = createAttacks(manager.getPlayer());
        currentAttack = null;
    }


    // Ketika meng-update:
    // 1. Tambah `phaseTimer` sebesar `delta`.
    // 2. Ambil `Boss` dari `manager` (cukup *get*, tidak perlu spawn lagi).
    // 3. Kalau Boss ada (tidak `null`), belum hancur, dan `phaseTimer >= getIntroSeconds()`:
    //    3.1. Bila `currentAttack` adalah `null`, buat agar boss bisa *collidable* dan kena *damage*, lalu mulai *attack* dengan index ke-0!
    //    3.2. Update `currentAttack` dengan parameter `boss`, `bulletManager` dari `manager`, dan `delta`.
    //    3.3. Bila `currentAttack` selesai, mulai attack boss lagi dengan memanggil helper untuk *pilih attack lain*.
    // 4. Hitung:
    //    - timeout = (manager.getPlayer() == null) ? TEST_TIMEOUT_SECONDS : getTimeoutSeconds(),
    //    - hpCutoffReached = (boss != null && boss.getHp() < getLifeThreshold()).
    // 5. Jika `hpCutoffReached || phaseTimer >= timeout`:
    //    5.1. Jika boss tidak `null`, tetapkan agar HP boss sesuai *life threshold*.
    //    5.2. Jika bulletManager dari `manager` tidak `null`, clear enemy bullet. (HINT: siapa ya yang berwenang atas bullet yang ada di screen?)
    //    5.3. Buat next state, dan pindah state dengan `manager`.

    public void update (LevelWaveManager manager, float delta){
        phaseTimer = delta;
        Boss boss = manager.getBoss();

        if (boss != null && !boss.isDestroyed() && phaseTimer >= getIntroSeconds()){
            if (currentAttack == null){
                boss.setDamageable(true);
                boss.setCollidable(true);
                currentAttack = attacks.get(0).get();
            }

            currentAttack.update(manager.getBoss(), manager.getBulletManager(), delta);

            if (currentAttack.isFinished()){
                pickAnotherAttack();
            }
        }

        float timeout = (manager.getPlayer() == null) ? TEST_TIMEOUT_SECONDS : getTimeoutSeconds();
        boolean hpCutoffReached = (boss != null && boss.getHp() < getLifeThreshold());

        if (hpCutoffReached || phaseTimer >= timeout){
            if (boss != null) {
                boss.setHp(getLifeThreshold());
            }
            if (manager.getBulletManager() != null){
                manager.getBulletManager().clearEnemyBullets();
            }

            manager.setState(createNextState());
        }
    }

    // ====================================================
    // Helper
    // ====================================================
    private void startAttack(Boss boss, int index) {
        currentIndex = index;
        currentAttack = attacks.get(index).get();
        boss.setMovementPattern(BossGlide.random(boss, GLIDE_DISTANCE, GLIDE_SECONDS, random));
    }

    private int pickAnotherAttack() {
        int next = random.nextInt(attacks.size() - 1);
        return (next >= currentIndex) ? next + 1 : next;
    }
}

