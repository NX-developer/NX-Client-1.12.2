package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;
import com.nx.client.settings.NumberSetting;

public class Speed extends Module {

    private final NumberSetting multiplier = new NumberSetting("Multiplier", 1.5, 1.0, 4.0, 0.1);

    public Speed() {
        super("Speed", "Moves you faster while walking on ground", Category.MOVEMENT);
        addSetting(multiplier);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.capabilities.isFlying) {
            return;
        }
        if (mc.player.movementInput.moveForward == 0.0F && mc.player.movementInput.moveStrafe == 0.0F) {
            return;
        }
        mc.player.motionX *= multiplier.getValue();
        mc.player.motionZ *= multiplier.getValue();
    }
}
