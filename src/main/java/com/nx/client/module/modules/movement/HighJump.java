package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;

public class HighJump extends Module {

    private final NumberSetting power = new NumberSetting("Power", 0.8, 0.42, 2.0, 0.01);

    public HighJump() {
        super("HighJump", "Jump much higher than normal", Category.MOVEMENT);
        addSetting(power);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.movementInput.jump && mc.player.onGround) {
            mc.player.motionY = power.getValue();
        }
    }
}
