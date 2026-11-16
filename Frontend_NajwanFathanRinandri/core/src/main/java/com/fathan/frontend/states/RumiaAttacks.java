// RumiaAttacks.java
package com.fathan.frontend.states;

import com.fathan.frontend.objects.GameObject;
import com.fathan.frontend.objects.patterns.bulletStrategy.BulletMovementPattern;
import com.fathan.frontend.objects.patterns.bulletStrategy.LinearBulletMovement;
import com.fathan.frontend.objects.patterns.bulletStrategy.StopAndTurnMovement;
import com.fathan.frontend.objects.patterns.shootingStrategy.RingShot;
import com.fathan.frontend.objects.patterns.shootingStrategy.SpreadShot;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public final class RumiaAttacks {
    private static final int DAMAGE = 10;
    private static final Random RANDOM = new Random();
    private static final BulletMovementPattern LINEAR = new LinearBulletMovement();

    private static final String RED = "bullet_danmaku_red";
    private static final String DARK_RED = "bullet_danmaku_purple";
    private static final String BLUE = "bullet_danmaku_blue";
    private static final String GREEN = "bullet_danmaku_green";
    private static final String YELLOW = "bullet_danmaku_orange";

    private RumiaAttacks() {}

    public static List<Supplier<Attack>> nonSpell1(GameObject player) {
        return List.of(
            () -> layeredLines(player),
            () -> alternatingRings(player),
            () -> riceSpear(player),
            () -> whip(player, RANDOM.nextBoolean())
        );
    }

    public static List<Supplier<Attack>> nonSpell2(GameObject player) {
        return List.of(
            () -> laserFan(player),
            () -> tripleRings(player),
            () -> oddEvenFans(player),
            () -> pairWhip(player, RANDOM.nextBoolean())
        );
    }

    public static Attack layeredLines(GameObject player) {
        Attack attack = new Attack("Sub12 Layered Aimed Lines");
        for (int k = 0; k < 7; k++) {
            attack.at(frames(12 + 8 * k),
                aimedFan(player, pxPerSec(4.0f), 1, 0f, (k % 2 == 0) ? RED : DARK_RED)
                    .withLayers(10, pxPerSec(zunSlowestLayer(4.0f, 1.0f, 10))));
        }
        return attack.thenRest(frames(120));
    }

    public static Attack alternatingRings(GameObject player) {
        Attack attack = new Attack("Sub13 Alternating Aimed Rings");
        float[] twelveWaySpeeds = {4.0f, 2.0f, 4.0f, 2.0f};
        for (int k = 0; k < 7; k++) {
            RingShot ring = (k % 2 == 0)
                ? new RingShot(pxPerSec(twelveWaySpeeds[k / 2]), 12, DAMAGE, LINEAR)
                : new RingShot(pxPerSec(3.0f), 16, DAMAGE, LINEAR);
            attack.at(frames(60 + 8 * k), ring.aimedAt(player).withSpriteKey(BLUE));
        }
        return attack.thenRest(frames(120));
    }

    public static Attack riceSpear(GameObject player) {
        return new Attack("Sub14 Rice Spear")
            .at(frames(80), aimedFan(player, pxPerSec(5.0f), 3, 3.75f, RED)
                .withLayers(16, pxPerSec(zunSlowestLayer(5.0f, 1.0f, 16))))
            .at(frames(110), new RingShot(pxPerSec(2.0f), 16, DAMAGE, LINEAR).aimedAt(player).withSpriteKey(BLUE))
            .thenRest(frames(90));
    }

    public static Attack whip(GameObject player, boolean mirrored) {
        Attack attack = new Attack("Sub15 Sweeping Whip");
        float sign = mirrored ? -1f : 1f;
        for (int i = 0; i < 16; i++) {
            float eclOffset = -15f + 3.75f * i;
            attack.at(frames(2 + 2 * i),
                aimedFan(player, pxPerSec(1.0f + 0.25f * i), 1, 0f, GREEN)
                    .withAngleOffset(sign * -eclOffset));
        }
        return attack.thenRest(frames(120));
    }

    public static Attack laserFan(GameObject player) {
        Attack attack = new Attack("Sub18 Laser Fan")
            .at(frames(12), aimedFan(player, pxPerSec(3.0f), 2, eclDeg(0.09817477f), GREEN)
                .withLayers(8, pxPerSec(zunSlowestLayer(3.0f, 1.0f, 8))));
        for (int k = 0; k < 6; k++) {
            attack.at(frames(20 + 8 * k), laserStandIn(player));
        }
        return attack.thenRest(frames(224 - 60));
    }

    public static Attack tripleRings(GameObject player) {
        return new Attack("Sub19 Triple Aimed Rings")
            .at(frames(60), new RingShot(pxPerSec(2.0f), 36, DAMAGE, LINEAR).aimedAt(player).withSpriteKey(GREEN))
            .at(frames(90), new RingShot(pxPerSec(2.6f), 28, DAMAGE, LINEAR).aimedAt(player).withSpriteKey(GREEN))
            .at(frames(120), new RingShot(pxPerSec(2.0f), 36, DAMAGE, LINEAR).aimedAt(player).withSpriteKey(GREEN))
            .thenRest(frames(120));
    }

    public static Attack oddEvenFans(GameObject player) {
        Attack attack = new Attack("Sub20 Odd/Even Aimed Fans");
        int[] ways = {8, 9, 10};
        for (int k = 0; k < ways.length; k++) {
            attack.at(frames(60 + 20 * k), aimedFan(player, pxPerSec(3.0f), ways[k], 15f, YELLOW)
                .withLayers(2, pxPerSec(zunSlowestLayer(3.0f, 1.0f, 2))));
        }
        return attack.thenRest(frames(120));
    }

    public static Attack pairWhip(GameObject player, boolean mirrored) {
        Attack attack = new Attack("Sub21 Sweeping Pair Whip");
        float sign = mirrored ? -1f : 1f;
        float step = eclDeg(0.14279966f);
        for (int i = 0; i < 16; i++) {
            float eclOffset = -eclDeg(0.7139983f) + step * i;
            attack.at(frames(2 + 2 * i),
                aimedFan(player, pxPerSec(1.0f + 0.25f * i), 2, step, GREEN)
                    .withAngleOffset(sign * -eclOffset));
        }
        return attack.thenRest(frames(120));
    }

    private static SpreadShot laserStandIn(GameObject player) {
        return aimedFan(player, pxPerSec(5.0f), 1, 0f, BLUE).withLayers(12, pxPerSec(2.0f));
    }

    public static Attack nightBirdWings(GameObject player) {
        Attack attack = new Attack("Sub22 Night Bird Wings");
        float[][] wings = {
            {-0.57119864f, 0.14279966f},
            {0.57119864f, -0.14279966f},
            {-0.7853982f, 0.19634955f},
            {0.7853982f, -0.19634955f}
        };
        int shot = 0;
        for (int set = 0; set < 2; set++) {
            for (float[] wing : wings) {
                for (int i = 0; i < 16; i++, shot++) {
                    float eclOffset = eclDeg(wing[0]) + eclDeg(wing[1]) * i;
                    attack.at(frames(2 + 2 * shot), aimedFan(player, pxPerSec(1.0f + 0.2f * i), 1, 0f, BLUE)
                        .withAngleOffset(-eclOffset));
                }
            }
        }
        return attack.thenRest(frames(100));
    }

    public static Attack demarcationRings(GameObject player) {
        Attack attack = new Attack("Sub23 Demarcation Rings");
        String[] colors = {BLUE, GREEN, RED};
        float[] firstRingTurn = {90f, -90f, 90f};
        for (int k = 0; k < 3; k++) {
            float time = frames(60 * k);
            attack.at(time, demarcationRing(player, colors[k], 0f, firstRingTurn[k]))
                .at(time, demarcationRing(player, colors[k], eclDeg(0.19634955f), -firstRingTurn[k]));
        }
        return attack.thenRest(frames(60));
    }

    public static Attack demarcationStreams(GameObject player) {
        Attack attack = new Attack("Sub23 Demarcation Streams");
        StopAndTurnMovement stopAndReaim = StopAndTurnMovement.reaimAt(frames(40), pxPerSec(3.0f), player);
        int shot = 0;
        for (int round = 0; round < 2; round++) {
            for (float side : new float[]{1f, -1f}) {
                for (int i = 0; i < 12; i++, shot++) {
                    float eclOffset = side * (-eclDeg(0.57119864f) + eclDeg(0.14279966f) * i);
                    attack.at(frames(2 + 2 * shot), new SpreadShot(pxPerSec(1.0f + 0.2f * i), 1, 0f, DAMAGE, 0f,
                        stopAndReaim, player, BLUE).withAngleOffset(-eclOffset));
                }
            }
        }
        return attack.thenRest(frames(60));
    }

    private static RingShot demarcationRing(GameObject player, String color, float eclOffsetDegrees, float eclTurnDegrees) {
        StopAndTurnMovement stopAndTurn = StopAndTurnMovement.turnBy(frames(40), pxPerSec(1.5f), -eclTurnDegrees);
        return new RingShot(pxPerSec(3.0f), 17, DAMAGE, stopAndTurn)
            .aimedAt(player)
            .withAngleOffset(-eclOffsetDegrees)
            .withSpriteKey(color);
    }

    private static SpreadShot aimedFan(GameObject player, float speed, int ways, float gapDegrees, String spriteKey) {
        return new SpreadShot(speed, ways, gapDegrees * (ways - 1), DAMAGE, 0f, LINEAR, player, spriteKey);
    }

    private static float zunSlowestLayer(float speed1, float speed2, int layers) {
        return speed1 - (speed1 - speed2) * (layers - 1) / layers;
    }

    private static float eclDeg(float radians) { return (float) Math.toDegrees(radians); }
    private static float pxPerSec(float pxPerFrame) { return pxPerFrame * 60f; }
    private static float frames(int frames) { return frames / 60f; }
}
