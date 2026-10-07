package com.fathan.frontend.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    // 1. Instans tunggal untuk Singleton
    private static AssetManager instance;

    // 2. Cache untuk Flyweight Pattern
    private Map<String, TextureRegion> textureRegionMap;
    private Map<String, Animation<TextureRegion>> animationMap;
    private Map<String, Texture> textureMap;

    // Cari tahu: kenapa Constructor AssetManager bersifat *private*?
    private AssetManager() {
        // TODO: Inisialisasi ketiga Map di atas sebagai HashMap kosong
        textureRegionMap = new HashMap<>();
        animationMap = new HashMap<>();
        textureMap = new HashMap<>();
    }

    // Titik akses global untuk mendapatkan instans tunggal
    public static AssetManager getInstance() {
        // TODO: Jika instance masih null, buat instance baru (Lazy Initialization)
        // Kembalikan referensi instance
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public Texture loadTexture(String filename) {
        // TODO:
        // 1. Periksa apakah filename sudah ada di dalam textureMap.
        // 2. Jika belum:
        //    - Pastikan Gdx.files != null dan file tersebut ada via Gdx.files.internal(filename).exists()
        //    - Buat Texture baru: new Texture(Gdx.files.internal(filename))
        //    - Simpan ke dalam textureMap dengan key filename
        //    - Jika file tidak ada / Gdx.files belum siap, kembalikan null
        // 3. Kembalikan Texture yang tersimpan
        if(!textureMap.containsKey(filename)){
            if (Gdx.files != null & Gdx.files.internal(filename).exists()){
                textureMap.put(filename, new Texture(Gdx.files.internal(filename)));
            }else {
                return null;
            }
        }
        return textureMap.get(filename);
    }

    // ========================================================================
    // Register the Region Textures
    // ========================================================================

    // Mendaftarkan region tunggal
    public void registerRegion(String key, TextureRegion region) {
        textureRegionMap.put(key, region);
    }

    // Memotong satu petak tertentu dari sprite sheet pada koordinat [row][col]
    public void registerRegionFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int col) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);
            // TODO: Simpan potongan grid[row][col] ke textureRegionMap dengan key ini
            textureRegionMap.put(key, grid[row][col]);
        }
    }

    // Overload praktis: mendaftarkan animasi mulai dari kolom 0 dengan PlayMode.LOOP
    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration) {
        registerAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP);
    }

    // Mendaftarkan serangkaian frame horizontal sebagai objek Animation
    public void registerAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            // TODO:
            // 1. Buat array TextureRegion[] sepanjang numFrames
            // 2. Isi array dengan grid[row][startCol + i]
            // 3. Buat Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            // 4. Set play mode animasi: anim.setPlayMode(playMode);
            // 5. Simpan anim ke animationMap dengan key
            // 6. Simpan frame pertama (frames[0]) ke textureRegionMap dengan key yang sama (sebagai sprite default)
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++){
                frames[i] = grid[row][startCol + i];
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key,anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // Overload untuk animasi yang dibalik arahnya (misal menghadap kiri dengan flip horizontal)
    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int numFrames, float frameDuration, boolean flipX, boolean flipY) {
        registerFlippedAnimationFromSheet(key, filename, tileWidth, tileHeight, row, 0, numFrames, frameDuration, Animation.PlayMode.LOOP, flipX, flipY);
    }

    public void registerFlippedAnimationFromSheet(String key, String filename, int tileWidth, int tileHeight, int row, int startCol, int numFrames, float frameDuration, Animation.PlayMode playMode, boolean flipX, boolean flipY) {
        Texture tex = loadTexture(filename);
        if (tex != null) {
            TextureRegion[][] grid = TextureRegion.split(tex, tileWidth, tileHeight);

            // TODO: Sama seperti registerAnimationFromSheet, tetapi setiap frame
            // di-copy via 'new TextureRegion(...)' lalu panggil 'frame.flip(flipX, flipY)'
            TextureRegion[] frames = new TextureRegion[numFrames];
            for (int i = 0; i < numFrames; i++){
                TextureRegion frame = new TextureRegion(grid[row][startCol + i]);
                frame.flip(flipX,flipY);
                frames[i] = frame;
            }

            Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
            anim.setPlayMode(playMode);

            animationMap.put(key,anim);
            textureRegionMap.put(key, frames[0]);
        }
    }

    // ========================================================================
    // Getting the Region Textures
    // ========================================================================

    // Mengambil region berdasarkan key
    public TextureRegion getTextureRegion(String key) {
        return textureRegionMap.get(key);
    }

    public TextureRegion getRegion(String key) {
        return getTextureRegion(key);
    }

    // Mengambil animasi berdasarkan key
    public Animation<TextureRegion> getAnimation(String key) {
        return animationMap.get(key);
    }

    public void init() {
        // Tip 1: Coba cari tahu row, startCol, numFrames, frameDuration, dan animation Animation.PlayMode yang menurut kalian enak secara estetik.
        // Tip 2: Di antara .png yang didaftarkan di bawah. Mana yang Spritesheet dan isi gambarnya adalah *animasi* dan mana yang isi gambarnya *diam saja*? Artinya apa?

        // TODO: Mendaftarkan animasi idle Reimu Hakurei (player.png: ukuran 32x48 per sel)
        registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0, 4, 0.12f);
        registerAnimationFromSheet("player_idle", "player.png", 32, 48, 0, 4, 0.12f);

        // TODO: Mendaftarkan animasi idle Boss (rumia.png: ukuran 64x64 per sel)
        registerAnimationFromSheet("boss_idle", "rumia.png", 64, 64, 0, 4, 0.15f);

        // TODO: Mendaftarkan animasi Fairy
        registerAnimationFromSheet("fairy_idle", "fairy.png", 32, 32, 0, 4, 0.12f);

        // TODO: Mendaftarkan peluru musuh (bullets_small.png: petak 16x16 baris 2 kolom 3)
        registerRegionFromSheet("bullet_danmaku", "bullet_small.png", 16,16, 2, 3);

        // TODO: Mendaftarkan 4 variasi Item (items.png: ukuran 16x16 per petak)
        registerRegionFromSheet("item_power", "items.png", 16, 16, 0, 0);
        registerRegionFromSheet("item_point", "items.png", 16, 16, 0, 1);
        registerRegionFromSheet("item_bomb", "items.png", 16, 16, 0, 3);
        registerRegionFromSheet("item_life", "items.png", 16, 16, 0, 5);

        registerAnimationFromSheet("player_idle", "player.png",32, 48, 0, 0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_left", "player.png",32, 48, 1, 0, 4, 0.12f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("player_right", "player.png",32, 48, 2, 0, 4, 0.12f, Animation.PlayMode.LOOP);

        registerAnimationFromSheet("boss_idle", "rumia.png", 64, 64, 0,0, 4, 0.2f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("boss_left", "rumia.png", 64, 64, 1,0, 4, 0.2f, Animation.PlayMode.REVERSED);
        registerAnimationFromSheet("boss_right", "rumia.png", 64, 64, 2,0, 4, 0.2f, Animation.PlayMode.LOOP);

        registerAnimationFromSheet("fairy_idle_red", "fairy.png", 32, 32, 1,0, 8, 0.125f, Animation.PlayMode.LOOP);
        registerAnimationFromSheet("fairy_idle_blue", "fairy.png", 32, 32, 0,0, 8, 0.125f, Animation.PlayMode.LOOP);

        // TODO: Tambahkan Registrasi Region sesuai tabel
        registerRegionFromSheet("bullet_amulet", "amulet_reimu.png", 16, 16, 0, 0);
        registerRegionFromSheet("bullet_amulet_homing", "amulet_reimu.png", 16, 16, 1, 0);
    }

    public void dispose() {
        // TODO: Lepaskan resource VRAM dari semua Texture yang dimuat, lalu kosongkan semua Map
        for (Texture texture : textureMap.values()){
            texture.dispose();
        }
        textureMap.clear();
        textureRegionMap.clear();
        animationMap.clear();
    }
}


