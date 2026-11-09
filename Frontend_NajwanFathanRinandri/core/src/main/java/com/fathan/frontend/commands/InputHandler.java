package com.fathan.frontend.commands;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.fathan.frontend.objects.GameObject;

import java.util.ArrayList;
import java.util.List;


public class InputHandler {

    // Configurable Key Bindings
    private int keyShoot = Input.Keys.Z;
    private int keyBomb = Input.Keys.X;
    private int keyFocus = Input.Keys.SHIFT_LEFT;

    // Bound Command Slots (Pre-allocated to prevent GC churn in 60 FPS game loop)
    private Command shootCommand = new ShootCommand();
    private BombCommand bombCommand = new BombCommand();
    private Command focusOn = new FocusCommand(true);
    private Command focusOff = new FocusCommand(false);
    private final MoveCommand moveCommand = new MoveCommand(0, 0);

    // Reusable command buffer returned by handleInput(delta)
    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {}

    public InputHandler(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    // ==========================================
    // KEY REBINDING & COMMAND BINDING API
    // ==========================================
    public void setKeyShoot(int keyShoot) { this.keyShoot = keyShoot; }
    public void setKeyBomb(int keyBomb) { this.keyBomb = keyBomb; }
    public void setKeyFocus(int keyFocus) { this.keyFocus = keyFocus; }

    public int getKeyShoot() { return keyShoot; }
    public int getKeyBomb() { return keyBomb; }
    public int getKeyFocus() { return keyFocus; }

    public void bindShootCommand(Command command) { this.shootCommand = command; }
    public void bindBombCommand(BombCommand command) { this.bombCommand = command; }
    public void bindFocusCommands(Command on, Command off) {
        this.focusOn = on;
        this.focusOff = off;
    }

    public void setEntities(List<GameObject> entities) {
        this.bombCommand.setEntities(entities);
    }

    // ==========================================
    // BOB NYSTROM PATTERN: INPUT TRANSLATION
    // ==========================================
    /**
     * Translates hardware inputs into an ordered list of Command objects for the current frame.
     * Pure Invoker: Does NOT know about Player, BulletManager, or specific game state.
     *
     * @param delta Frame delta time
     * @return Reusable list of commands issued this frame
     */
    public List<Command> handleInput(float delta) {
        activeCommands.clear();
        if (Gdx.input == null) return activeCommands;

        // 1. Focus Mode Toggle (Shift key)
        boolean isFocusHeld = Gdx.input.isKeyPressed(keyFocus) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        activeCommands.add(isFocusHeld ? focusOn : focusOff);

        // 2. 8-Directional Movement (WASD or Arrow keys)
        float dx = 0;
        float dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        float speedMult = isFocusHeld ? 0.45f : 1.0f;
        moveCommand.set(dx * delta * speedMult, dy * delta * speedMult);
        activeCommands.add(moveCommand);

        // 3. Shoot Command (Holding Z key - continuous stream)
        if (Gdx.input.isKeyPressed(keyShoot)) {
            activeCommands.add(shootCommand);
        }

        // 4. Bomb / Spell Card Command (X key - tap once)
        if (Gdx.input.isKeyJustPressed(keyBomb)) {
            activeCommands.add(bombCommand);
        }

        return activeCommands;
    }
}
