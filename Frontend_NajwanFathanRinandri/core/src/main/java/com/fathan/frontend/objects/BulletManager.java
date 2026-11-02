package com.fathan.frontend.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fathan.frontend.objects.bullets.Bullet;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.items.ItemType;
import com.fathan.frontend.systems.EntityFactory;

import java.util.*;



public class BulletManager {
    // 1. Antrean Pool untuk Daur Ulang Objek (Object Pool Pattern)
    private Queue<Bullet> playerBulletPool;
    private Queue<Bullet> enemyBulletPool;

    // 2. Daftar Peluru Aktif di Layar
    private List<Bullet> activePlayerBullets;
    private List<Bullet> activeEnemyBullets;

    public BulletManager() {
        // TODO:
        // TODO:
        // 1. Inisialisasi playerBulletPool dan enemyBulletPool sebagai LinkedList kosong
        this.playerBulletPool = new LinkedList<>();
        this.enemyBulletPool = new LinkedList<>();

        // 2. Inisialisasi activePlayerBullets dan activeEnemyBullets sebagai ArrayList kosong
        this.activePlayerBullets = new ArrayList<>();
        this.activeEnemyBullets = new ArrayList<>();
    }

    // Peminjaman bullet Pemain dari Pool (5 poin)
    public Bullet spawnPlayerBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah playerBulletPool tidak kosong.
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        // 2. Re-initialize bullet.
        // 3. Tambahkan bullet ke dalam daftar activePlayerBullets.
        // 4. Kembalikan instance bullet tersebut.
        if (!playerBulletPool.isEmpty()){
            bullet = playerBulletPool.poll();
        } else{
            bullet = EntityFactory.createPlayerBullet(x, y, damage);
            activePlayerBullets.add(bullet);
        }
        return bullet;
    }

    // Peminjaman bullet Musuh dari Pool (5 poin)
    public Bullet spawnEnemyBullet(float x, float y, float vx, float vy, int damage) {
        Bullet bullet;
        // TODO:
        // 1. Cek apakah enemyBulletPool tidak kosong:
        //    Kalau ya, poll dari pool. Kalau tidak, buat baru menggunakan Factory.
        // 2. Re-initialize bullet.
        // 3. Tambahkan bullet ke dalam daftar activeEnemyBullets
        // 4. Kembalikan instance bullet tersebut

        if (!enemyBulletPool.isEmpty()){
            bullet = enemyBulletPool.poll();
        } else{
            bullet = EntityFactory.createEnemyBullet(x, y, damage);
            activeEnemyBullets.add(bullet);
        }
        return bullet;
    }

    public void clearEnemyBullets(List<GameObject> entities) {
        // TODO:
        // 1. Buat Iterator<Bullet> untuk menelusuri activeEnemyBullets secara aman.
        Iterator<Bullet> eIter = activeEnemyBullets.iterator();
        // 2. Selama masih ada elemen berikutnya di iterator:
        //    - Ambil objek Bullet saat ini.
        //    - Jika parameter entities tidak bernilai null:
        //      * Buat objek Item bertipe ItemType.POINT menggunakan EntityFactory.createItem(...)
        //        pada posisi (bullet.getX(), bullet.getY()), lalu tambahkan ke dalam list entities.
        //    - Tandai bullet sebagai hancur (bullet.destroy()).
        //    - Hapus bullet dari activeEnemyBullets menggunakan remove() pada iterator di step 1.
        //    - Kembalikan bullet ke dalam antrean enemyBulletPool menggunakan offer().
        while (eIter.hasNext()) {
            Bullet bullet = eIter.next();

            if (entities != null) {
                Item point = EntityFactory.createItem(bullet.getX(), bullet.getY(), ItemType.POINT);
                entities.add(point);
            }

            bullet.destroy();
            eIter.remove();
            enemyBulletPool.offer(bullet);
        }
        // TIP: Lihat rough outlinenya di `clearPlayerBullets()` di bawah
    }

    public void clearEnemyBullets() {
        clearEnemyBullets(null);
    }

    // Pembersihan seluruh peluru pemain saat restart permainan (Copy-Paste):
    public void clearPlayerBullets() {
        Iterator<Bullet> pIter = activePlayerBullets.iterator();
        while (pIter.hasNext()) {
            Bullet bullet = pIter.next();
            bullet.destroy();
            pIter.remove();
            playerBulletPool.offer(bullet);
        }
    }

    public void update(float delta, float screenWidth, float screenHeight) {
        /// 1. Daur ulang & pergerakan bullet pemain (5 poin)
        // TODO:
        // - Copy-paste loop iterator yang ada di updateAndClean() di Main.java
        // - Ubah class yang di-referensikan oleh iterator sebagai `Bullet` daripada T. Dan ganti list yang direferensikan menjadi activePlayerBullets
        // - Ganti pengecekannya menjadi apakah bullet berada di luar layar
        //   ATAU bullet sudah ditandai hancur
        //   - Jika YA:
        //     * Tandai bullet sebagai hancur (destroy)
        //     * Hapus bullet dari daftar bullet aktif secara aman
        //     * Kembalikan bullet ke antrean pool

        Iterator<Bullet> iterator = activePlayerBullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.isDestroyed();
                iterator.remove();
                playerBulletPool.offer(bullet);
            }
        }

        // 2. Daur ulang & pergerakan bullet musuh (5 poin)
        // TODO:
        // - Lakukan prosedur yang sama persis seperti di atas terhadap activeEnemyBullets
        // - bullet musuh yang keluar layar atau hancur dikembalikan ke enemyBulletPool via offer()
        Iterator<Bullet> iterator2 = activeEnemyBullets.iterator();
        while (iterator2.hasNext()) {
            Bullet bullet = iterator2.next();
            bullet.update(delta);

            if (bullet.isOffScreen(screenWidth, screenHeight) || bullet.isDestroyed()) {
                bullet.isDestroyed();
                iterator2.remove();
                enemyBulletPool.offer(bullet);
            }
        }

    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activePlayerBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
        for (Bullet b : activeEnemyBullets) {
            if (!b.isDestroyed()) b.render(batch);
        }
    }

    // Metode pembantu akses pool dan daftar aktif
    public List<Bullet> getActivePlayerBullets() { return activePlayerBullets; }
    public List<Bullet> getActiveEnemyBullets() { return activeEnemyBullets; }
    public int getPlayerPoolSize() { return playerBulletPool.size(); }
    public int getEnemyPoolSize() { return enemyBulletPool.size(); }
}

