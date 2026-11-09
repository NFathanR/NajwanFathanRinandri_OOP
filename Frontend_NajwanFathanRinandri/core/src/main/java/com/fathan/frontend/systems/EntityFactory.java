package com.fathan.frontend.systems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fathan.frontend.objects.Player;
import com.fathan.frontend.objects.bullets.Bullet;
import com.fathan.frontend.objects.bullets.BulletType;
import com.fathan.frontend.objects.enemies.Boss;
import com.fathan.frontend.objects.enemies.Fairy;
import com.fathan.frontend.objects.items.Item;
import com.fathan.frontend.objects.items.ItemType;

public class EntityFactory {

    public static Player createPlayer(float x, float y, String name, int hp, int power, int spellCards) {
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("player_idle");
        Player player = new Player(x, y, name, hp, power, spellCards);
        player.setAnimation(idleAnim);
        return player;
    }

    public static Fairy createFairy(float x, float y, String name, int hp) {
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("fairy_idle_red");
        Fairy fairy = new Fairy(x, y, name, hp);
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Fairy createFairy(float x, float y, String name, int hp, String keyString) {
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation(keyString);
        Fairy fairy = new Fairy(x, y, name, hp);
        fairy.setAnimation(idleAnim);
        return fairy;
    }

    public static Boss createBoss(float x, float y, String name, int hp) {
        Animation<TextureRegion> idleAnim = AssetManager.getInstance().getAnimation("boss_idle");
        Boss boss = new Boss(x, y, name, hp);
        boss.setAnimation(idleAnim);
        return boss;
    }

    public static Item createItem(float x, float y, ItemType itemType) {
        String key = switch (itemType) {
            case POWER -> "item_power";
            case POINT -> "item_point";
            case BOMB  -> "item_bomb";
            case LIFE  -> "item_life";
        };
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        Item item = new Item(x, y, itemType);
        item.setSprite(sprite);
        return item;
    }

    public static Bullet createPlayerBullet(float x, float y, int damage, String spriteKey) {
        String key = (spriteKey != null) ? spriteKey : "bullet_amulet";
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key);
        Bullet bullet = new Bullet(x, y, BulletType.AMULET, damage);
        bullet.setSprite(sprite);
        return bullet;
    }

    public static Bullet createPlayerBullet(float x, float y, int damage) {
        return createPlayerBullet(x, y, damage, "bullet_amulet");
    }

    // [UBAH] Perbarui method createEnemyBullet bawaan Modul 8 dengan perubahan berikut:
    public static Bullet createEnemyBullet(float x, float y, int damage, String spriteKey) { // Tambahin String spriteKey sebagai parameter
        String key = (spriteKey != null) ? spriteKey : "bullet_danmaku"; // Tambahkan variabel String lokal key
        TextureRegion sprite = AssetManager.getInstance().getTextureRegion(key); // Ganti "bullet_danmaku" menjadi key tadi
        Bullet bullet = new Bullet(x, y, 0f, BulletType.DANMAKU, damage);
        bullet.setSprite(sprite);
        return bullet;
    }

    // [TAMBAHAN] Tambahkan method overload 3 parameter berikut (mendelegasikan ke method 4 parameter di atas):
    public static Bullet createEnemyBullet(float x, float y, int damage) {
        return createEnemyBullet(x, y, damage, "bullet_danmaku");
    }

}
