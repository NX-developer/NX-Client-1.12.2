package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;

public class Step extends Module {

    private final NumberSetting height = new NumberSetting("Height", 1.0, 0.6, 2.0, 0.1);

    public Step() {
        super("Step", "Automatically step up full blocks", Category.MOVEMENT);
        addSetting(height);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.stepHeight = 0.6F;
        }
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.onGround) {
            mc.player.stepHeight = height.getValue().floatValue();
        }
    }
}
