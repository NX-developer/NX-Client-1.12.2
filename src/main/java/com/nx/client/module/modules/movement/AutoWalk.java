package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", "Holds the forward key for you automatically", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        }
    }

    @Override
    public void onTick() {
        if (mc.gameSettings != null && mc.currentScreen == null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }
}
