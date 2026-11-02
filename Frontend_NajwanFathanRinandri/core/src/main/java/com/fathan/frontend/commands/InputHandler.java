package com.fathan.frontend.commands;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.fathan.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;


public class InputHandler {

    // 1. Konfigurasi Tombol
    // TODO 1: Buat field private bertipe int untuk:
    // - keyShoot
    // - keyBomb
    // - keyFocus
    // Lalu isi masing-masing attribut dengan sebuah key dari Input.Keys sesuai dengan kesukaan kalian. (Contoh: `int keyShoot = Input.Keys.Z`)
    // Ini adalah tombol yang kalian harus tekan di keyboard kalian untuk melakukan aksi-aksi tersebut.
    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_RIGHT;

    // 2. Slot Command Pre-allocated (Zero-GC Invoker)
    // TODO 2: Buat field private untuk:
    // - shootCommand
    // - bombCommand
    // - focusOn dengan parameter inisialisasi `true`
    // - focusOff dengan parameter inisialisasi `false`
    // - moveCommand, khusus Move Command bertipe `final` dengan parameter awal (0, 0)!
    // Sesuaikan tipe class dengan Command Pattern yang sesuai dan inisialisasi `new` class Command Pattern yang sesuai. (Contoh: `ShootCommand shootCommand = new ShootCommand()`)
    private ShootCommand shootCommand = new ShootCommand();
    private BombCommand bombCommand = new BombCommand();
    private FocusCommand focusOn = new FocusCommand(true);
    private FocusCommand focusOff = new FocusCommand(false);
    private final MoveCommand moveCommand = new MoveCommand(0,0);


    // TODO 3: Buat field private final untuk list darCommand dengan nama activeCommands, lalu inisialisasi dengan ArrayList baru.
    // Field ini nanti akan dipakai untuk buffer atas command-command yang harus dibentuk di CS.
    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {}

    public InputHandler(List<GameObject> entities) {
        this.bombCommand.setEntities(entities); // Pastikan ini tidak error!
    }

    public void setEntities(List<GameObject> entities) {
        this.bombCommand.setEntities(entities); // Pastikan ini tidak error!
    }

    // Getter dan Setter untuk Key Rebinding
    // TODO 4: Buat getter & setter untuk setiap key yang anda buat di TODO 1.
    int getKeyShoot() {
        return keyShoot;
    }

    int getKeyBomb() {
        return keyBomb;
    }

    int getKeyFocus() {
        return keyFocus;
    }

    void setKeyShoot(int keyShoot) {
        this.keyShoot = keyShoot;
    }

    void setKeyBomb(int keyBomb) {
        this.keyBomb = keyBomb;
    }

    void setKeyFocus(int keyFocus) {
        this.keyFocus = keyFocus;
    }

    public List<Command> handleInput(float delta) {
        activeCommands.clear();
        if (Gdx.input == null) return activeCommands;

        // 1. Focus Mode Toggle (Shift Kiri atau Kanan) (3 poin)
        // TODO:
        // - Cek apakah tombol keyFocus atau SHIFT_RIGHT sedang ditekan (isKeyPressed).
        // - Simpan ke variabel boolean `isFocusHeld`.
        // - Tambahkan (add) ke activeCommands: jika isFocusHeld maka focusOn, jika tidak maka focusOff.
        Boolean isFocusHeld = (Gdx.input.isKeyPressed(keyFocus) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT));

        activeCommands.add(isFocusHeld ? focusOn : focusOff);

        // 2. Pergerakan 8-Arah (WASD atau Arrow Keys) (5 poin)
        float dx = 0;
        float dy = 0;
        // TODO:
        // - Jika W atau UP ditekan (isKeyPressed), tambahkan dy sebesar 1
        // - Jika S atau DOWN ditekan (isKeyPressed), kurangi dy sebesar 1
        // - Jika A atau LEFT ditekan (isKeyPressed), kurangi dx sebesar 1
        // - Jika D atau RIGHT ditekan (isKeyPressed), tambahkan dx sebesar 1
        // - Hitung float speedMult = isFocusHeld ? 0.45f : 1.0f;
        // - Perbarui perpindahan: moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
        // - Tambahkan moveCommand ke dalam activeCommands.
        if (Gdx.input != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                dy += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                dy -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                dx += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                dx -= 1;
            }
        }

        float speedMult = isFocusHeld ? 0.45f : 1.0f;

        moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
        activeCommands.add(moveCommand);

        // 3. Shoot Command (Tahan tombol untuk tembakan beruntun) (3 poin)
        // TODO:
        // - Cek apakah keyShoot sedang ditekan (isKeyPressed).
        // - Jika YA: tambahkan shootCommand ke dalam activeCommands.
        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        // 4. Bomb / Spell Card Command (Sekali tekan) (3 poin)
        // TODO:
        // - Cek apakah keyBomb baru saja ditekan (isKeyJustPressed).
        // - Jika YA: tambahkan bombCommand ke dalam activeCommands.
        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}
