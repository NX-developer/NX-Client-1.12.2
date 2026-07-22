package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class AutoSneak extends Module {

    public AutoSneak() {
        super("AutoSneak", "Keeps you sneaking automatically", Category.MOVEMENT);
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }

    @Override
    public void onTick() {
        if (mc.gameSettings != null && mc.currentScreen == null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        }
    }
}
