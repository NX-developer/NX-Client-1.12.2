package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class AutoSprint extends Module {

    public AutoSprint() {
        super("AutoSprint", "Always sprint automatically while moving forward", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameSettings == null) {
            return;
        }
        boolean shouldSprint = mc.player.movementInput.moveForward > 0.0F
                && !mc.player.isSneaking()
                && !mc.player.isHandActive()
                && !mc.player.collidedHorizontally;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), shouldSprint);
        if (shouldSprint) {
            mc.player.setSprinting(true);
        }
    }
}
