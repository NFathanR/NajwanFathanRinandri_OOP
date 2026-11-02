package com.fathan.frontend.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.fathan.frontend.objects.bullets.Bullet;
import com.fathan.frontend.objects.bullets.BulletType;
import com.fathan.frontend.objects.enemies.Enemy;
import com.fathan.frontend.objects.items.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.fathan.frontend.objects.items.ItemType;
import com.badlogic.gdx.graphics.Color;
import com.fathan.frontend.observers.GameObserver;
import com.fathan.frontend.systems.AssetManager;
import com.fathan.frontend.systems.EntityFactory;
import com.fathan.frontend.objects.patterns.ShootingPattern;

import java.util.ArrayList;
import java.util.List;


public class Player extends GameObject {
    private String name;
    private int hp;
    private int lives = 3;
    private int power;
    private int spellCards;
    private long score;
    private int currentDir = 0;
    private ShootingPattern shootingPattern;
    int grazeCount = 0;
    boolean focused = false;
    float shootTimer = 0.1f;
    float shootCooldown = 0.1f;
    float invulnerableTimer = 0f;

    // TODO: Deklarasikan atribut private List<GameObserver> observers dan inisialisasi dengan ArrayList kosong
    private List<GameObserver> observers = new ArrayList<>();

    public Player(String name, int hp, int power, int spellCards) {
        super(280, 40, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    public Player(float x, float y, String name, int hp, int power, int spellCards) {
        super(x, y, 32, 48, 200f, Color.RED);
        this.name = name;
        this.hp = hp;
        this.power = power;
        this.spellCards = spellCards;
        this.score = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta); // Advances stateTime for idle animations
        shootTimer += delta;

        if (invulnerableTimer > 0f) {
            invulnerableTimer -= delta;
            if (invulnerableTimer < 0f) invulnerableTimer = 0f;
        }
    }

    public void updateAnimationState(float dx) {
        AssetManager assets = AssetManager.getInstance();
        Animation<TextureRegion> anim;
        if (dx < 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan -1.
            // 2. Ubah currentDir menjadi -1.
            // 3. Ambil animasi "player_left" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            if (currentDir != -1){
                currentDir = -1;
                anim = assets.getAnimation("player_left");

                if (anim != null){
                    setAnimation(anim);
                }
            }

        } else if (dx > 0) {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 1.
            // 2. Ubah currentDir menjadi 1.
            // 3. Ambil animasi "player_right" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).

            if (currentDir != 1){
                currentDir = 1;
                anim = assets.getAnimation("player_right");

                if (anim != null){
                    setAnimation(anim);
                }
            }
        } else {
            // TODO:
            // 1. Lanjutkan perubahan hanya jika currentDir bukan 0.
            // 2. Ubah currentDir menjadi 0.
            // 3. Ambil animasi "player_idle" melalui assets.getAnimation(...).
            //    Simpan pada variabel lokal bertipe Animation<TextureRegion> bernama anim.
            // 4. Jika anim tidak null, pasang anim melalui setAnimation(...).
            if (currentDir != 1){
                currentDir = 1;
                anim = assets.getAnimation("player_idle");

                if (anim != null){
                    setAnimation(anim);
                }
            }
        }
    }

    public void shootBullet(BulletManager bulletManager) {
        if (bulletManager == null) return;

        // TODO 1: Cek apakah shootTimer sudah lebih besar atau sama dengan shootCooldown.
        if (shootTimer >= shootCooldown) {
            if (shootingPattern != null) {
                shootingPattern.execute(x + width / 2f - 8f, y + height, bulletManager, true);
            } else {
                bulletManager.spawnPlayerBullet(x + width / 2f - 8f, y + height, 0, 400f, 10 + power);
            }
        }
        shootTimer = 0;
        // TODO 2: Reset shootTimer kembali ke 0
    }

    // Eksekusi Spell Card (Bomb) dari BombCommand
    public void useBomb(BulletManager bulletManager, List<GameObject> entities) {
        // TODO:
        // 1. Cek apakah spellCards > 0.
        // 2. Jika YA:
        //    - Kurangi spellCards sebanyak 1 (spellCards--).
        //    - Aktifkan status kebal selama 5 detik: invulnerableTimer = 5.0f;
        //    - Jika bulletManager tidak null, panggil bulletManager.clearEnemyBullets(entities).
        //    - Cetak pesan ke konsol: name + " unleashes SPELL CARD (Fantasy Seal)! All enemy bullets cleared!"
        if (spellCards > 0) {
            spellCards--;
            notifyGrazeChanged();
            invulnerableTimer = 5.0f;
            if (bulletManager != null) {
                bulletManager.clearEnemyBullets(entities);
            }
            System.out.println(name + " unleashes SPELL CARD (Fantasy Seal)! All enemy bullets cleared!");
        } else {
            System.out.println("No Spell Cards (Bombs) remaining!");
        }
        // 3. Jika TIDAK:
        //    - Cetak pesan ke konsol: "No Spell Cards (Bombs) remaining!"
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch != null && active && !isDestroyed()) {
            // Animasi berkedip frekuensi tinggi saat kebal (i-frames respawn / bomb)
            if (invulnerableTimer > 0f) {
                boolean visible = ((int) (invulnerableTimer * 16)) % 2 == 0;
                if (!visible) return; // Lewati gambar frame agar terjadi efek kedip
            }

            if (focused) {
                batch.setColor(1f, 1f, 1f, 0.5f); // Opasitas 50% saat Focus Mode aktif
            }
            super.render(batch);
            if (focused) {
                batch.setColor(1f, 1f, 1f, 1.0f); // Kembalikan opasitas ke 100%
            }
        }
    }

    // Menggambar titik indikator Core Hitbox saat Focus Mode (Shift) aktif
    public void renderFocusIndicator(ShapeRenderer shapeRenderer) {
        if (focused && shapeRenderer != null && active) {
            Rectangle core = getCoreHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 4f);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(core.x + core.width / 2f, core.y + core.height / 2f, 2f);
            shapeRenderer.end();
        }
    }

    boolean isFocused () {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getGrazeCount() {
        return grazeCount;
    }

    public void useBomb(BulletManager bulletManager) {
        useBomb(bulletManager, null);
    }

    public void addGraze() {
        // TODO:
        // 1. Tambahkan grazeCount sebanyak 1 (this.grazeCount++)
        // 2. Tambahkan skor sebesar 50 menggunakan addScore(50)
        grazeCount++;
        addScore(50);
        notifyGrazeChanged();
    }

    public void moveUp(float delta) { this.y += speed * delta; }
    public void moveDown(float delta) { this.y -= speed * delta; }
    public void moveLeft(float delta) { this.x -= speed * delta; }
    public void moveRight(float delta) { this.x += speed * delta; }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Item item) {
            System.out.println("Player touches items");
            collectItem(item);
        }
    }

    public void shoot(Enemy target) {
        int damage = 10 + power;
        System.out.println(name + " shoots " + target.getName() + " dealing " + damage + " DMG!");
        boolean defeated = target.takeDamage(damage);
        if (defeated) {
            addScore(target.getScoreValue());
        }
    }

    public void move(float dx, float dy) {
        // TODO:
        // 1. Tambahkan posisi x sebesar dx dikali speed (this.x += dx * speed)
        this.x += dx*speed;
        // 2. Tambahkan posisi y sebesar dy dikali speed (this.y += dy * speed)
        this.y += dy*speed;
        // 3. Panggil metode clampToPlayfield()
        clampToPlayfield();
    }

    private void clampToPlayfield() {
        float minX = 32;
        float maxX = 416 - width;
        float minY = 16;
        float maxY = 560 - height;

        // TODO: Batasi nilai x agar tidak kurang dari minX dan tidak lebih dari maxX,
        //       serta nilai y agar tidak kurang dari minY dan tidak lebih dari maxY.

        if (this.x < minX) this.x = minX;
        if (this.x > maxX) this.x = maxX;
        if (this.y < minY) this.y = minY;
        if (this.y > maxY) this.y = maxY;
    }

    public void takeDamage(int damage, BulletManager bulletManager, List<GameObject> entities) {
        if (isInvulnerable() || isDestroyed()) {
            if (isInvulnerable()) {
                System.out.println(name + " is INVULNERABLE! (0 damage taken)");
            }
            return;
        }
        this.hp -= damage;
        notifyHpChanged();
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(name + " took " + damage + " damage! Remaining HP: " + this.hp);
        if (this.hp == 0) {
            System.out.println(name + " was defeated (Pichuun~)! ");
            loseLife(bulletManager, entities);
        }
    }

    public void addScore(long points) {
        if (points > 0) {
            this.score += points;
            notifyScoreChanged();
            System.out.println(name + " gained " + points + " pts! Total Score: " + this.score);
        }
    }


    @Override
    public Rectangle getCoreHitbox() {
        // TODO: Kembalikan objek Rectangle baru berukuran 8x8 yang berpusat tepat di tengah sprite:
        // - koordinat X: x + (width / 2f) - 4f
        // - koordinat Y: y + (height / 2f) - 4f
        // - lebar: 8f, tinggi: 8f
        return new Rectangle(x + (width / 2f) - 4f, y + (height / 2f) - 4f, 8f, 8f);
    }

    @Override
    public Rectangle getGrazeHitbox() {
        // TODO: Kembalikan objek Rectangle baru dengan koordinat posisi (x, y)
        //       dan ukuran seluas sprite visual karakter (width, height)
        return new Rectangle(x, y, width, height);
    }

    public void registerObserver(GameObserver observer) {
        // TODO:
        // 1. Cek apakah observer tidak null DAN belum ada di dalam list observers (!observers.contains(observer)).
        // 2. Jika memenuhi syarat:
        //    - Tambahkan observer ke dalam list observers.
        //    - Pancarkan seluruh state awal pemain saat ini ke observer tersebut:
        //      * observer.onScoreChanged(score);
        //      * observer.onHpChanged(hp);
        //      * observer.onLivesChanged(lives);
        //      * observer.onSpellCardsChanged(spellCards);
        //      * observer.onPowerChanged(power);
        //      * observer.onGrazeChanged(grazeCount);
        if (observer != null && !observers.contains(observer)){
            observers.add(observer);
            observer.onScoreChanged(score);
            observer.onHpChanged(hp);
            observer.onLivesChanged(lives);
            observer.onSpellCardsChanged(spellCards);
            observer.onPowerChanged(power);
            observer.onGrazeChanged(grazeCount);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Metode Pemancar Notifikasi ke Seluruh Observer (4 poin)
    // TODO: Buat 6 metode private void berikut yang MELAKUKAN LOOP KE SELURUH ELEMEN DI 'observers'
    //       dan memanggil metode callback yang bersesuaian:
    // 1. notifyScoreChanged()      -> memanggil obs.onScoreChanged(score)
    // 2. notifyHpChanged()         -> memanggil obs.onHpChanged(hp)
    // 3. notifyLivesChanged()      -> memanggil obs.onLivesChanged(lives) (Sistem Nyawa Touhou)
    // 4. notifySpellCardsChanged() -> memanggil obs.onSpellCardsChanged(spellCards)
    // 5. notifyPowerChanged()      -> memanggil obs.onPowerChanged(power)
    // 6. notifyGrazeChanged()      -> memanggil obs.onGrazeChanged(grazeCount)


    private void notifyScoreChanged(){
        for(GameObserver obs : observers) {
            obs.onScoreChanged(score);
        }
    }

    private void notifyHpChanged() {
        for(GameObserver obs : observers) {
            obs.onHpChanged(hp);
        }
    }

    private void notifyLivesChanged() {
        for(GameObserver obs : observers) {
            obs.onLivesChanged(lives);
        }
    }

    private void notifySpellCardsChanged() {
        for(GameObserver obs : observers) {
            obs.onSpellCardsChanged(spellCards);
        }
    }

    private void notifyPowerChanged() {
        for(GameObserver obs : observers) {
            obs.onPowerChanged(power);
        }
    }

    private void notifyGrazeChanged() {
        for(GameObserver obs : observers) {
            obs.onGrazeChanged(grazeCount);
        }
    }

    public void loseLife(BulletManager bulletManager, List<GameObject> entities) {
        this.lives--;
        notifyLivesChanged();

        if (bulletManager != null) {
            bulletManager.clearEnemyBullets();
        }

        if (this.lives > 0) {
            if (bulletManager != null) {
                System.out.println("[Player] " + name + " lost a life! Remaining Lives: " + this.lives + ". Respawning at (200, 50)...");
            }
            respawn();
        } else {
            if (bulletManager != null) {
                System.out.println("[Player] GAME OVER! " + name + " has run out of lives!");
            }
            destroy();
        }
    }

    public void respawn() {
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.hp = 100;
        this.invulnerableTimer = 3.0f;
        this.power = Math.max(10, this.power - 16); // Penalti death Touhou
        this.spellCards = 3; // Reset bomb menjadi 3 saat respawn
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
    }

    public void resetGame() {
        this.lives = 3;
        this.hp = 100;
        this.power = 15;
        this.spellCards = 3;
        this.score = 0;
        this.grazeCount = 0;
        this.active = true;
        this.x = 200;
        this.y = 50;
        this.invulnerableTimer = 3.0f;
        notifyLivesChanged();
        notifyHpChanged();
        notifyPowerChanged();
        notifySpellCardsChanged();
        notifyScoreChanged();
        notifyGrazeChanged();
    }

    public void collectItem(Item item) {
        if (item.isDestroyed()) return;

        ItemType type = item.getItemTypeEnum();
        if (type != null) {
            switch (type) {
                case POWER -> {
                    this.power += type.getPowerBonus();
                    notifyPowerChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POWER item! Power increased to " + power);
                }
                case POINT -> {
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected POINT item!");
                }
                case BOMB -> {
                    this.spellCards++;
                    notifySpellCardsChanged();
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected BOMB item! SpellCards: " + spellCards);
                }
                case LIFE -> {
                    this.lives++;   // Perhatikan! Ada tambahan ini!
                    notifyLivesChanged();
                    this.hp = 100;
                    addScore(item.getScoreValue());
                    System.out.println(name + " collected LIFE item! Remaining Lives: " + lives);
                }
            }
        } else {
            addScore(item.getScoreValue());
            System.out.println(name + " collected " + item.getItemType() + "!");
        }

        item.destroy();
    }

    public boolean isAlive() {
        return this.hp > 0 && this.lives > 0 && !isDestroyed();
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0f;
    }

    public float getInvulnerableTimer() {
        return invulnerableTimer;
    }

    public int getLives() { return lives; }
    public void setLives(int lives) {
        this.lives = lives;
        notifyLivesChanged();
    }

    // Encapsulation getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
        notifyHpChanged();
    }

    public int getPower() { return power; }
    public void setPower(int power) {
        this.power = power;
        notifyPowerChanged();
    }

    public int getSpellCards() { return spellCards; }
    public void setSpellCards(int spellCards) {
        this.spellCards = spellCards;
        notifySpellCardsChanged();
    }

    public long getScore() { return score; }

    public ShootingPattern getShootingPattern() {return shootingPattern; }
    public void setShootingPattern(ShootingPattern shootingPattern) { this.shootingPattern = shootingPattern; }
}
