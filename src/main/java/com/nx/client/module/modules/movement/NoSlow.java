package com.nx.client.module.modules.movement;

import com.nx.client.module.Category;
import com.nx.client.module.Module;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow", "Removes the slowdown while eating or using items", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (mc.player.isHandActive()) {
            mc.player.movementInput.moveForward *= 5.0F;
            mc.player.movementInput.moveStrafe *= 5.0F;
        }
    }
}
