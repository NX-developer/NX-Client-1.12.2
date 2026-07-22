package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;

public class Glide extends Module {

    private final NumberSetting fallSpeed = new NumberSetting("FallSpeed", 0.05, 0.0, 0.2, 0.01);

    public Glide() {
        super("Glide", "Slowly float down instead of falling fast", Category.MOVEMENT);
        addSetting(fallSpeed);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.onGround || mc.player.capabilities.isFlying || mc.player.isInWater()) {
            return;
        }
        if (mc.player.motionY < 0.0) {
            mc.player.motionY = -fallSpeed.getValue();
            mc.player.fallDistance = 0.0F;
        }
    }
}
